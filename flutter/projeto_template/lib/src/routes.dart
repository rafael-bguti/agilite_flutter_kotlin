import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:projeto_estudo/ClientesScreen.dart';
import 'package:projeto_estudo/DashboardScreen.dart';

import 'fake_services.dart';

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
    '/clientes',
    inFullLayout: true,
    (_, __) => const ClientesScreen(),
  ),
];
