import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

const _emailKey = "rot2010_email";

abstract class LoginService {
  Future<void> login(String email, String password);
  Future<String?> loadSavedEmail();
}

class ServerLoginServiceAdapter implements LoginService {
  final HttpProvider _httpProvider;
  final AuthService _authService;
  final Storage _storage;

  ServerLoginServiceAdapter(this._httpProvider, this._authService, this._storage);

  Future<void> login(String email, String password) async {
    final userData = "$email:$password";
    final response = await _httpProvider.post(
      '/public/cas2010',
      body: userData.ecodeToBase64(),
    );

    final user = LoggedUser.fromJson(response.bodyMap);
    fullLayoutFacade.setMenuItems(user.menu);

    _storage.put(_emailKey, email);
    _authService.signIn(user);
  }

  Future<String?> loadSavedEmail() async {
    return _storage.get(_emailKey);
  }
}
