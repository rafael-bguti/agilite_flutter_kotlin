import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';

import 'menu.dart';

class FakeLoginService extends LoginService {
  @override
  Future<bool> login(String email, String password) async {
    fullLayoutFacade.setMenuItems(menuItens);

    final user = LoggedUser(
      nome: 'Rafael',
      email: 'agilite@agilite.com',
      token: '123456',
      refreshToken: '654321',
      empresa: 'Lojinha Xpto',
      menu: menuItens,
    );

    authService.signIn(user);
    return true;
  }

  @override
  Future<String?> loadSavedEmail() async {
    return "teste@agilite.com";
  }
}
