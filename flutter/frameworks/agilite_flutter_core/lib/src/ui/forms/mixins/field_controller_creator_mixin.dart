import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

mixin FieldControllerCreatorMixin {
  T createFieldController<T extends FieldController<dynamic>>(
    BuildContext context,
    T? controller,
    String? name,
    T Function() controllerBuilderWhenNull,
    void Function(T controller)? onControllerCreated,
  ) {
    if (controller != null) return controller;

    final formState = context.findAncestorStateOfType<AFormState>();
    if (formState == null) {
      final result = _buildNewController(controllerBuilderWhenNull);
      onControllerCreated?.call(result);
      return result;
    } else {
      final mappedController = formState.controller.getController(name!);
      if (mappedController != null) {
        if (mappedController is T) {
          return mappedController;
        } else {
          throw 'Já existe um controller com a key $name no form e ele não é compatível com o tipo ${T.toString()}';
        }
      }

      final result = _buildNewController(controllerBuilderWhenNull);
      formState.controller.addController(result);
      onControllerCreated?.call(result);
      return result;
    }
  }
}

T _buildNewController<T extends FieldController<dynamic>>(T Function() builder) {
  final controller = builder();
  controller.constructedBy = ConstructorType.auto;

  return controller;
}
