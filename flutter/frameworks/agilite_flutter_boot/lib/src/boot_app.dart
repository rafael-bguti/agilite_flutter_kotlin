import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';

import 'boot_theme.dart';
import 'services/auth_service.dart';
import 'storages/storage.dart';

class BootApp extends StatefulWidget {
  final String? storageName;
  final Widget Function(bool toAppBar)? appLogoBuilder;
  final String appTitle;

  final ThemeMode? themeMode;
  final ThemeData? lightTheme;
  final ThemeData? darkTheme;

  final List<ARoute>? routes;

  final HttpProvider? customHttpProvider;

  const BootApp({
    this.storageName,
    this.appTitle = 'Agilite',
    this.appLogoBuilder,
    this.themeMode,
    this.lightTheme,
    this.darkTheme,
    this.routes,
    this.customHttpProvider,
    super.key,
  });

  @override
  State<BootApp> createState() => _BootAppState();
}

class _BootAppState extends State<BootApp> {
  @override
  void initState() {
    super.initState();
    if (widget.customHttpProvider != null) {
      _httpProviderCache = widget.customHttpProvider;
    }

    metadataRepository = HttpMetadataRepositoryAdapter(httpProvider);
    coreHttpProvider = httpProvider;

    if (widget.themeMode != null) {
      themeNotifier.value = widget.themeMode!;
    }
  }

  @override
  Widget build(BuildContext context) {
    _storageName ??= widget.storageName;

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
            routerConfig: buildRouterConfig(widget.routes ?? <ARoute>[]),
            title: widget.appTitle,
            themeMode: themeMode,
            theme: widget.lightTheme ?? buildLightTheme(),
            darkTheme: widget.darkTheme ?? buildDarkTheme(),
          ),
        );
      },
    );
  }
}

Storage get storage => _storageCache ??= HiveStorage(_storageName ?? 'new_agilite_boot');
AuthService get authService => _authServiceCache ??= StorageAuthServiceImpl(storage);
HttpProvider get httpProvider => _httpProviderCache ??= HttpProviderImpl(authorizationTokenGetter: () => authService.loadLoggedToken());
SseProvider get sseProvider => _sseProviderCache ??= SseProviderImpl();

Storage? _storageCache;
AuthService? _authServiceCache;
HttpProvider? _httpProviderCache;
SseProvider? _sseProviderCache;

String? _storageName;
