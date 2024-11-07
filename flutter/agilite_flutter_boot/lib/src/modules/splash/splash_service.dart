import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

class SplashService {
  final AuthService _authService;
  SplashService(this._authService);

  Future<String> loadNextRoute() async {
    await Future.delayed(const Duration(seconds: 1));

    final user = await _authService.loadLoggedUser();
    if (user == null) return loginPath;

    fullLayoutFacade.setMenuItems(user.menu);
    return dashboardPath;
  }
}
