import 'dart:async';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import '../ui/a_future_builder.dart';

typedef RouteGuard = String? Function(BuildContext context, GoRouterState state);

class ARoute {
  final String? name;
  final String path;
  final List<RouteGuard> routeGuards;
  final List<ARoute>? children;
  final bool inFullLayout;

  final Widget Function(BuildContext context, GoRouterState state)? eagerPageBuilder;
  final Future<Widget> Function(BuildContext context, GoRouterState state)? lazyPageBuilder;

  const ARoute.lazy(
    this.path,
    this.lazyPageBuilder, {
    this.name,
    this.routeGuards = const [],
    this.children,
    this.inFullLayout = true,
  }) : eagerPageBuilder = null;

  const ARoute.eager(
    this.path,
    this.eagerPageBuilder, {
    this.name,
    this.routeGuards = const [],
    this.children,
    this.inFullLayout = false,
  }) : lazyPageBuilder = null;

  GoRoute get route => GoRoute(
        name: name,
        path: path,
        routes: children?.map((e) => e.route).toList() ?? [],
        pageBuilder: (context, state) => CustomTransitionPage(
          child: _build(context, state),
          key: state.pageKey,
          transitionDuration: kThemeAnimationDuration,
          transitionsBuilder: (context, animation, secondaryAnimation, child) {
            return FadeTransition(
              opacity: CurveTween(curve: Curves.easeInOutCirc).animate(animation),
              child: child,
            );
          },
        ),
        redirect: (context, state) {
          for (final routeGuard in routeGuards) {
            final redirect = routeGuard.call(context, state);
            if (redirect != null) {
              return redirect;
            }
          }
          return null;
        },
      );

  Widget _build(BuildContext context, GoRouterState state) {
    return AgiliteAppScaffold(
      child: eagerPageBuilder?.call(context, state) ??
          AFutureBuilder(
            future: lazyPageBuilder!(context, state),
          ),
    );
  }
}
