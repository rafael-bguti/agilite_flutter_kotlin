import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:scf/tasks/scf2010/scf2010.dart' deferred as scf2010;
import 'package:scf/tasks/scf2011/scf2011.dart' deferred as scf2011;
import 'package:srf/tasks/srf2030/srf2030.dart' deferred as srf2030;
import 'package:srf/tasks/srf2050/srf2050.dart' deferred as srf2050;
import 'package:srf/tasks/srf2051/srf2051.dart' deferred as srf2051;
import 'package:srf/tasks/srf2060/srf2060.dart' deferred as srf2060;

import 'pages/dashboard_page.dart';

final routes = <ARoute>[
  ARoute.eager(
    '/sdui/:base',
    inFullLayout: true,
    (_, state) {
      final base = state.pathParameters['base'];
      return SduiLoader(
        key: ValueKey(base),
        contentProvider: RemoteSduiContentProvider(
          url: '/sdui/$base',
          pathParams: state.pathParameters,
        ),
      );
    },
  ),
  ARoute.eager(
    splashPath,
    (_, __) => const SplashScreen(),
  ),
  ARoute.eager(
    loginPath,
    (_, __) => const LoginScreen(),
  ),
  ARoute.eager(
    dashboardPath,
    inFullLayout: true,
    (_, __) => const DashboardPage(),
  ),
  ARoute.lazy(
    '/scf2010',
    (_, __) async {
      await scf2010.loadLibrary();
      return scf2010.Scf2010();
    },
  ),
  ARoute.lazy(
    '/scf2011',
    (_, __) async {
      await scf2011.loadLibrary();
      return scf2011.Scf2011();
    },
  ),
  ARoute.lazy(
    '/srf2030',
    (_, __) async {
      await srf2030.loadLibrary();
      return srf2030.Srf2030();
    },
  ),
  ARoute.lazy(
    '/srf2050',
    (_, __) async {
      await srf2050.loadLibrary();
      return srf2050.Srf2050();
    },
  ),
  ARoute.lazy(
    '/srf2051',
    (_, __) async {
      await srf2051.loadLibrary();
      return srf2051.Srf2051();
    },
  ),
  ARoute.lazy(
    '/srf2060',
    (_, __) async {
      await srf2060.loadLibrary();
      return srf2060.Srf2060();
    },
  ),
];
