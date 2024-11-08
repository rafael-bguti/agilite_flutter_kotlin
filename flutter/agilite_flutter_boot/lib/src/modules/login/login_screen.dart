import 'package:agilite_flutter_boot/boot.dart';
import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';

const _kFormHeight = 600.0;
const _kFormWidth = 500.0;
String emailInputHint = 'E-Mail';

class LoginScreen extends StatefulWidget {
  final LoginService? service;
  const LoginScreen({
    this.service,
    super.key,
  });

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> with SingleTickerProviderStateMixin {
  late final AnimationController _rippleController;
  late final Animation<double> _rippleAnimation;
  late final LoginService _service = widget.service ?? ServerLoginServiceAdapter(httpProvider, authService, storage);

  final _userController = TextEditingController();
  final _passwordController = TextEditingController();
  final _passwordFocusNode = FocusNode();

  String? _errorMessage;
  bool _hidePassword = true;
  bool _loading = false;
  @override
  void initState() {
    super.initState();

    _service.loadSavedEmail().then((email) {
      if (email != null && _userController.text.isEmpty) {
        _userController.text = email;
        _passwordFocusNode.requestFocus();
      }
    });

    _rippleController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 350),
    );

    _rippleAnimation = Tween<double>(begin: 0.0, end: 1.4).animate(
      CurvedAnimation(
        parent: _rippleController,
        curve: Curves.easeInOut,
      ),
    )..addListener(
        () {
          if (_rippleAnimation.status == AnimationStatus.completed) {
            context.go(dashboardPath);
          }
        },
      );
  }

  @override
  void dispose() {
    _rippleController.dispose();
    _userController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final brightness = MediaQuery.of(context).platformBrightness;
    return Stack(
      children: [
        DecoratedBox(
          decoration: BoxDecoration(
            image: DecorationImage(
              image: AssetImage(coreStyle.assetLoginBackground ?? ''),
              onError: (exception, stackTrace) {},
              fit: BoxFit.cover,
            ),
          ),
          child: Center(
            child: Padding(
              padding: const EdgeInsets.all(8.0),
              child: Material(
                elevation: 9,
                borderRadius: BorderRadius.circular(8),
                child: Container(
                  constraints: const BoxConstraints.tightFor(
                    height: _kFormHeight,
                    width: _kFormWidth,
                  ),
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(8),
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      coreStyle.getLogoWidget(LogoDestination.drawer, Theme.of(context).brightness),
                      ASpacingColumn(
                        children: [
                          Text('Bem vindo!', style: textTheme?.headlineMedium),
                          Text('Faça o login para continuar', style: textTheme?.labelLarge),
                        ],
                      ),
                      Focus(
                        onKey: (node, event) {
                          if (event.logicalKey == LogicalKeyboardKey.enter) {
                            _btnEntrarPressed();
                          }
                          return KeyEventResult.ignored;
                        },
                        child: Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 24.0),
                          child: ASpacingColumn(
                            children: [
                              TextField(
                                autofocus: true,
                                controller: _userController,
                                decoration: _createInputDecoration(
                                  prefixIcon: Icons.email,
                                  hintText: emailInputHint,
                                ),
                                style: textTheme?.titleMedium,
                              ),
                              TextField(
                                style: textTheme?.titleMedium,
                                obscureText: _hidePassword,
                                controller: _passwordController,
                                focusNode: _passwordFocusNode,
                                decoration: _createInputDecoration(
                                  prefixIcon: Icons.security_rounded,
                                  hintText: 'Senha',
                                ).copyWith(
                                  suffixIcon: IconButton(
                                    onPressed: () {
                                      setState(() {
                                        _hidePassword = !_hidePassword;
                                      });
                                    },
                                    icon: _hidePassword ? const Icon(Icons.visibility_off) : const Icon(Icons.visibility),
                                  ),
                                ),
                              ),
                              Row(
                                mainAxisAlignment: MainAxisAlignment.end,
                                children: <Widget>[
                                  TextButton(
                                    onPressed: () {
                                      context.go('/cgs1010/25');
                                    },
                                    //TODO: Implementar esqueceu sua senha
                                    child: const Text('Esqueceu sua senha?'),
                                  ),
                                ],
                              ),
                              const SizedBox(height: 16),
                              if (_errorMessage != null)
                                Text(
                                  _errorMessage!,
                                  style: textTheme?.titleLarge?.copyWith(color: errorColor, fontWeight: FontWeight.bold),
                                ),
                              ConstrainedBox(
                                constraints: const BoxConstraints(
                                  minWidth: double.infinity,
                                  minHeight: 45,
                                ),
                                child: TextButton(
                                  style: TextButton.styleFrom(
                                    backgroundColor: onBackgroundColor,
                                    padding: EdgeInsets.all(coreStyle.kPaddingM),
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(4),
                                    ),
                                  ),
                                  onPressed: _loading ? null : _btnEntrarPressed,
                                  child: _loginButonBody(context),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
        AnimatedBuilder(
          animation: _rippleAnimation,
          builder: (_, __) {
            return Ripple(value: _rippleAnimation.value);
          },
        ),
      ],
    );
  }

  void _btnEntrarPressed() async {
    if (_userController.text.isEmpty || _passwordController.text.isEmpty) {
      setState(() {
        _errorMessage = 'Informe o e-mail e a senha para entrar';
      });
      return;
    }
    setState(() {
      _errorMessage = null;
    });

    try {
      setState(() {
        _loading = true;
      });
      await _service.login(_userController.text, _passwordController.text);
      _rippleController.forward();
    } on BadRequestException catch (e) {
      setState(() {
        _errorMessage = e.toString();
      });
    } on UnauthenticatedException {
      setState(() {
        _errorMessage = 'Usuário ou senha inválidos';
      });
    } catch (e, s) {
      AError.defaultCatch(e, s);
    } finally {
      setState(() {
        _loading = false;
      });
    }
  }

  Widget _loginButonBody(BuildContext context) {
    if (_loading) {
      return SizedBox(
        height: 24,
        width: 24,
        child: CircularProgressIndicator(
          valueColor: AlwaysStoppedAnimation<Color>(backgroundColor),
        ),
      );
    } else {
      return Text(
        'ENTRAR',
        style: textTheme?.titleMedium!.copyWith(
          fontWeight: FontWeight.bold,
          color: backgroundColor,
        ),
      );
    }
  }

  InputDecoration _createInputDecoration({
    required IconData prefixIcon,
    required String hintText,
  }) {
    final themeColor = Theme.of(context).colorScheme;
    return InputDecoration(
      focusedBorder: OutlineInputBorder(
        borderSide: BorderSide(color: themeColor.onSurface.withOpacity(0.6)),
      ),
      enabledBorder: OutlineInputBorder(
        borderSide: BorderSide(color: themeColor.onSurface.withOpacity(0.12)),
      ),
      errorBorder: OutlineInputBorder(
        borderSide: BorderSide(color: themeColor.error.withOpacity(0.2)),
      ),
      focusedErrorBorder: OutlineInputBorder(
        borderSide: BorderSide(color: themeColor.error.withOpacity(0.7)),
      ),
      hintStyle: const TextStyle(
        fontWeight: FontWeight.w500,
      ),
      prefixIcon: Icon(
        prefixIcon,
      ),
      filled: true,
      hintText: hintText,
    );
  }
}

class Ripple extends StatelessWidget {
  final double value;

  const Ripple({
    super.key,
    required this.value,
  });

  @override
  Widget build(BuildContext context) {
    final screenWidth = MediaQuery.of(context).size.width;
    final screenHeight = MediaQuery.of(context).size.height;

    double radius = screenHeight * value;
    return Positioned(
      left: screenWidth / 2 - radius,
      bottom: 2 * coreStyle.kPaddingL - radius,
      child: Container(
        width: 2 * radius,
        height: 2 * radius,
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          color: colorScheme?.surface,
        ),
      ),
    );
  }
}
