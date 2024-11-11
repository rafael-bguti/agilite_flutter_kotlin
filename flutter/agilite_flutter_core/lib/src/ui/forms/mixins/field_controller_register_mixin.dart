import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

mixin FieldControllerRegisterMixin {
  T registerFormField<T extends FieldController<dynamic>>(
    BuildContext context,
    T? controller,
    String? name,
    T Function() controllerBuilderWhenNull,
    void Function(T controller)? onControllerCreated,
  ) {
    if (controller != null && controller.addedToFormController) return controller;

    if (controller?.createdBySpread ?? false) return controller!;

    final formState = context.findAncestorStateOfType<AFormState>();
    if (formState == null) {
      if (controller == null) {
        throw 'Ao criar um AField sem o controller é obrigatório que ele esteja dentro de um AForm. AField que deu erro: $name';
      }
      return controller;
    }

    if (controller != null) {
      formState.addController(controller);
      return controller;
    }

    final mappedController = formState.getControllerByName(name!);
    if (mappedController != null) {
      if (mappedController is T) {
        return mappedController;
      } else {
        throw 'Já existe um controller com a key $name no form e ele não é compatível com o tipo ${T.toString()}';
      }
    }

    controller = controllerBuilderWhenNull();
    final result = formState.addController(controller);
    onControllerCreated?.call(result);

    return result;
  }
}
