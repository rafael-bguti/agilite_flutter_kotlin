import 'package:agilite_flutter_core/core.dart';

typedef FieldValidator = String? Function(dynamic value);

class Validations {
  static String? isNotEmpty(dynamic value, [String message = "Este campo é obrigatório"]) {
    if (value == null) {
      return message;
    }
    if (value is String) {
      if (value.isEmpty) {
        return message;
      }
    } else if (value is List) {
      if (value.isEmpty) {
        return message;
      }
    }

    return null;
  }

  static String? isEmail(dynamic value, [String message = "Informe um e-mail válido"]) {
    if (value == null || value.toString().isEmpty) {
      return null;
    }
    final bool emailValid = RegExp(r"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$").hasMatch(value.toString());
    if (!emailValid) {
      return message;
    }
    return null;
  }

  static FieldValidator minLength(int minLength, [String? message]) {
    return (dynamic value) {
      if (value == null || value.toString().isEmpty) {
        return null;
      }
      if (value.toString().length < minLength) {
        return message ?? "Este campo deve ter no mínimo $minLength caracteres";
      }
      return null;
    };
  }

  static FieldValidator maxValue(double maxValue, [String? message]) {
    return (dynamic value) {
      if (value == null) {
        return null;
      }

      if (value! is double) {
        return "Este campo deve ser numérico";
      }

      if ((value as double) > maxValue) {
        final String value;
        if (maxValue.abs() != maxValue) {
          value = maxValue.format();
        } else {
          value = maxValue.toInt().toString();
        }

        return message ?? "O valor desse campo deve ser de no máximo $value";
      }
      return null;
    };
  }
}

class ValidationMapper {
  static List<FieldValidator>? parseValidationQuery(String? validationsQuery) {
    if (validationsQuery == null) {
      return null;
    }

    final List<String> validations = validationsQuery.splitToList(",");

    final List<FieldValidator> validators = [];

    for (final String validation in validations) {
      if (validation == "required") {
        validators.add(Validations.isNotEmpty);
      } else if (validation.startsWith("minLength")) {
        final int minLength = int.parse(validation.split(":")[1]);
        validators.add(Validations.minLength(minLength));
      } else if (validation == "email") {
        validators.add(Validations.isEmail);
      } else if (validation.startsWith("maxValue")) {
        final double maxValue = double.parse(validation.split(":")[1]);
        validators.add(Validations.maxValue(maxValue));
      }
    }

    return validators.isEmpty ? null : validators;
  }
}
