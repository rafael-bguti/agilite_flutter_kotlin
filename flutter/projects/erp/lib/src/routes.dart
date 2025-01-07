import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:scf/tasks/scf2011/scf2011.dart' deferred as scf2011;

import 'pages/dashboard_page.dart';

final routes = <ARoute>[
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
    '/scf2011',
    (_, __) async {
      await scf2011.loadLibrary();
      return scf2011.Scf2011();
    },
  ),
];
