import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'fake_services.dart';
import 'modules/alerts_screen.dart';
import 'modules/buttons_screen.dart';
import 'modules/cards_screen.dart';
import 'modules/crud/crud_vendas.dart';
import 'modules/dashboard_screen.dart';
import 'modules/forms_screen.dart';
import 'modules/tables/tables_screen.dart';
import 'modules/tabs_screen.dart';

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
    '/tabs',
    inFullLayout: true,
    (_, __) => const TabsScreen(),
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
  ARoute.eager(
    '/tables',
    inFullLayout: true,
    (_, __) => const TablesScreen(),
  ),
  ARoute.eager(
    '/forms',
    inFullLayout: true,
    (_, __) => const FormsScreen(),
  ),
  ARoute.eager(
    '/vendas',
    inFullLayout: true,
    (_, __) => const CrudVendas(),
  ),
  ARoute.eager(
    '/sdui/:name',
    inFullLayout: true,
    (_, state) {
      final name = state.pathParameters['name'];
      return SduiLoader(
        contentProvider: RemoteSduiContentProvider(
          url: '/sdui/task/$name',
          pathParams: state.pathParameters,
        ),
      );
    },
  ),
];
