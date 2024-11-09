import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:projeto_estudo/src/modules/alerts_screen.dart';
import 'package:projeto_estudo/src/modules/buttons_screen.dart';
import 'package:projeto_estudo/src/modules/cards_screen.dart';

import 'fake_services.dart';
import 'modules/dashboard_screen.dart';

final routes = <ARoute>[
  ARoute.eager(
    splashPath,
    (_, __) => const SplashScreen(),
  ),
  ARoute.eager(
    loginPath,
    (_, __) => LoginScreen(
      service: FakeLoginService(),
    ),
  ),
  ARoute.eager(
    dashboardPath,
    inFullLayout: true,
    (_, __) => const DashboardScreen(),
  ),
  ARoute.eager(
    '/buttons',
    inFullLayout: true,
    (_, __) => const ButtonsScreen(),
  ),
  ARoute.eager(
    '/cards',
    inFullLayout: true,
    (_, __) => const CardsScreen(),
  ),
  ARoute.eager(
    '/alerts',
    inFullLayout: true,
    (_, __) => const AlertsScreen(),
  ),
];
