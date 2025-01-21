import 'package:agilite_flutter_core/core.dart';

class Srf2030Controller extends ViewController<Srf2030State> {
  Srf2030Controller() : super(Srf2030InitialState());

  final formFilterController = FormController();

  @override
  void dispose() {
    formFilterController.dispose();

    super.dispose();
  }

  Future<void> processarJson() async {}
}

sealed class Srf2030State {}

class Srf2030InitialState extends Srf2030State {}
