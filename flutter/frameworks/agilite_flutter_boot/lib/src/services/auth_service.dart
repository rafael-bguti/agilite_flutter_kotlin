import 'dart:async';
import 'dart:convert';

import '../models/logged_user.dart';
import '../storages/storage.dart';

abstract class AuthService {
  LoggedUser? get loggedUser;
  FutureOr<LoggedUser?> loadLoggedUser();
  FutureOr<String?> loadLoggedToken();
  Future<void> signIn(LoggedUser user);
  Future<void> signOut();
}

class StorageAuthServiceImpl extends AuthService {
  LoggedUser? _loggedUser;
  static const _userKey = "user";

  final Storage _secureStorage; // TODO - MVP - Está utilizando um Storage sem segurança, precisa alterar para um Storage seguro
  StorageAuthServiceImpl(this._secureStorage);

  @override
  LoggedUser? get loggedUser => _loggedUser;

  @override
  FutureOr<LoggedUser?> loadLoggedUser() {
    if (_loggedUser != null) return _loggedUser;

    return _secureStorage.get(_userKey).then((json) {
      if (json == null) return null;

      _loggedUser = LoggedUser.fromJson(jsonDecode(json));
      return _loggedUser!;
    });
  }

  @override
  FutureOr<String?> loadLoggedToken() async {
    if (_loggedUser != null) return 'Bearer ${_loggedUser!.token}';
    final loggedUser = await loadLoggedUser();
    return loggedUser == null ? null : 'Bearer ${loggedUser.token}';
  }

  @override
  Future<void> signIn(LoggedUser user) async {
    await _secureStorage.delete(_userKey);
    await _secureStorage.put(_userKey, jsonEncode(user.toJson()));

    _loggedUser = user;
  }

  @override
  Future<void> signOut() async {
    await _secureStorage.delete(_userKey);

    _loggedUser = null;
  }
}
