import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'boot_theme.dart';
import 'services/auth_service.dart';
import 'storages/storage.dart';

final ValueNotifier<ThemeMode> themeNotifier = ValueNotifier(ThemeMode.system);

class BootApp extends StatelessWidget {
  final String? storageName;
  final Widget Function(bool toAppBar)? appLogoBuilder;
  final String appTitle;

  final ThemeMode? themeMode;
  final ThemeData? lightTheme;
  final ThemeData? darkTheme;

  final List<ARoute>? routes;

  const BootApp({
    this.storageName,
    this.appTitle = 'Agilite',
    this.appLogoBuilder,
    this.themeMode,
    this.lightTheme,
    this.darkTheme,
    this.routes,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    _storageName ??= storageName;

    return ValueListenableBuilder<ThemeMode>(
      valueListenable: themeNotifier,
      builder: (context, themeMode, _) {
        return KeyedSubtree(
          key: ValueKey(themeMode),
          child: MaterialApp.router(
            localizationsDelegates: const [
              GlobalMaterialLocalizations.delegate,
              GlobalCupertinoLocalizations.delegate,
              GlobalWidgetsLocalizations.delegate,
            ],
            supportedLocales: const [Locale('pt', 'BR'), Locale('en')],
            routerConfig: buildRouterConfig(routes ?? <ARoute>[]),
            title: appTitle,
            themeMode: themeMode,
            theme: lightTheme ?? buildLightTheme(),
            darkTheme: darkTheme ?? buildDarkTheme(),
          ),
        );
      },
    );
  }
}

Storage get storage => _storageCache ??= HiveStorage(_storageName ?? 'agilite_boot');
AuthService get authService => _authServiceCache ??= StorageAuthServiceImpl(storage);
HttpProvider get httpProvider => _httpProviderCache ??= HttpProviderImpl(authorizationTokenGetter: () => authService.loadLoggedToken());

Storage? _storageCache;
AuthService? _authServiceCache;
HttpProvider? _httpProviderCache;

String? _storageName;
