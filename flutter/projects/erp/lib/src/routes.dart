import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

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
];
