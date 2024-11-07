import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

String splashPath = '/';
String loginPath = '/login';
String dashboardPath = '/dashboard';

final globalRouterObserver = GlobalNavigationObserver();

GoRouter? _cachedRouter;
GoRouter get appRouter => _cachedRouter!;

GoRouter buildRouterConfig(List<ARoute> routes) {
  GoRouter.optionURLReflectsImperativeAPIs = true;

  List<ARoute> routesWithFullLayout = routes.where((e) => e.inFullLayout).toList();
  List<ARoute> routesWithoutFullLayout = routes.where((e) => !e.inFullLayout).toList();

  return _cachedRouter ??= GoRouter(
    initialLocation: loginPath,
    routes: <RouteBase>[
      ...(routesWithoutFullLayout.map((e) => e.route).toList()),
      ShellRoute(
        builder: (BuildContext context, GoRouterState state, Widget child) {
          return AFullLayout(child: child);
        },
        routes: [
          ...(routesWithFullLayout.map((e) => e.route).toList()),
        ],
      )
    ],
    redirect: _handleRedirect,
    debugLogDiagnostics: true,
    navigatorKey: globalNavigatorKey,
    observers: [
      globalRouterObserver,
    ],
  );
}

bool _routerInitialized = false;
String? _handleRedirect(BuildContext context, GoRouterState state) {
  if (_routerInitialized) return null;
  _routerInitialized = true;

  if (state.uri.toString() != '/') {
    return '/';
  }

  return null;
}

class GlobalNavigationObserver extends NavigatorObserver {
  List<Route<dynamic>> routeStack = [];

  @override
  void didPush(Route<dynamic> route, Route<dynamic>? previousRoute) {
    routeStack.add(route);
    print("Stack depois de push: ${routeStack.map((e) => e.settings.name)}");
  }

  @override
  void didPop(Route<dynamic> route, Route<dynamic>? previousRoute) {
    routeStack.removeLast();
    print("Stack depois de pop: ${routeStack.map((e) => e.settings.name)}");
  }

  @override
  void didRemove(Route<dynamic> route, Route<dynamic>? previousRoute) {
    routeStack.removeLast();
    print("Stack depois de remove: ${routeStack.map((e) => e.settings.name)}");
  }

  @override
  void didReplace({Route<dynamic>? newRoute, Route<dynamic>? oldRoute}) {
    routeStack.clear();
    if (newRoute != null) {
      routeStack.add(newRoute);
    }
    print("Stack depois de replace: ${routeStack.map((e) => e.settings.name)}");
  }

  int get stackLength => routeStack.length;
}
