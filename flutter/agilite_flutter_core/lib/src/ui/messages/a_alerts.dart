import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

enum AlertType {
  warning,
  error,
  info,
  success,
}

enum RenderType { light, soft, strong }

class AAlert extends StatelessWidget {
  final String message;
  final String? title;
  final AlertType type;
  final RenderType renderType;

  const AAlert._(
    this.message,
    this.type,
    this.title,
    this.renderType, {
    super.key,
  });

  const AAlert.warning({
    required this.message,
    this.title,
    this.renderType = RenderType.soft,
    super.key,
  }) : type = AlertType.warning;

  const AAlert.error({
    required this.message,
    this.title,
    this.renderType = RenderType.soft,
    super.key,
  }) : type = AlertType.error;

  const AAlert.info({
    required this.message,
    this.title,
    this.renderType = RenderType.soft,
    super.key,
  }) : type = AlertType.info;

  const AAlert.success({
    required this.message,
    this.title,
    this.renderType = RenderType.soft,
    super.key,
  }) : type = AlertType.success;

  @override
  Widget build(BuildContext context) {
    final color = _getBackColor(renderType);
    return ACard(
      showShadow: false,
      padding: const EdgeInsets.all(8),
      backgroundColor: color,
      borderColor: _getBorderColor(),
      leftBorderColor: _getLeftCardColor(),
      body: Row(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(
            _getIcon(),
            color: _getForeColor(),
          ),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                if (title != null)
                  Text(
                    title!,
                    style: textTheme?.labelLarge?.copyWith(
                      color: _getForeColor(),
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                SelectableText(
                  message,
                  style: textTheme?.labelLarge?.copyWith(
                    color: _getForeColor(),
                    fontWeight: FontWeight.normal,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  IconData _getIcon() {
    switch (type) {
      case AlertType.warning:
        return Icons.warning;
      case AlertType.error:
        return Icons.error;
      case AlertType.info:
        return Icons.info;
      case AlertType.success:
        return Icons.check_circle;
      default:
        return Icons.info;
    }
  }

  Color? _getBorderColor() {
    if (renderType == RenderType.soft) {
      return _getBackColor(renderType).darkenIfLightOrLightenIfDark(brightness);
    } else if (renderType == RenderType.strong) {
      return _getBackColor(renderType);
    } else {
      return null;
    }
  }

  Color? _getLeftCardColor() {
    if (renderType != RenderType.light) {
      return null;
    } else {
      return _getBackColor(RenderType.strong);
    }
  }

  Color _getBackColor(RenderType renderType) {
    if (renderType == RenderType.light) {
      return backgroundColor;
    }
    switch (type) {
      case AlertType.warning:
        return renderType == RenderType.soft ? const Color(0xFFFAEED8) : const Color(0xFFE4A93C);
      case AlertType.error:
        return renderType == RenderType.soft ? const Color(0xFFF9D9D9) : const Color(0xFFE23D3D);
      case AlertType.info:
        return renderType == RenderType.soft ? const Color(0xFFDBECFC) : const Color(0xFF4AA2EE);
      case AlertType.success:
        return renderType == RenderType.soft ? const Color(0xFFCCF2F3) : const Color(0xFF00BCC2);
      default:
        return Colors.transparent;
    }
  }

  Color _getForeColor() {
    if (renderType == RenderType.light) {
      return onBackgroundColor;
    }
    if (renderType == RenderType.strong) {
      return Colors.white;
    }
    return const Color(0xFF303840);
  }
}
