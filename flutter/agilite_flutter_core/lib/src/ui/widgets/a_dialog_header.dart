import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class ADialogHeader extends StatelessWidget {
  final Widget? headerIcon;
  final String? headerText;
  final Widget? headerOptions;
  final VoidCallback? onClose;

  const ADialogHeader({
    Key? key,
    this.headerIcon,
    this.headerText,
    this.headerOptions,
    this.onClose,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 8.0),
      child: ASpacingRow(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          Expanded(
            child: Text(
              headerText ?? '',
              style: Theme.of(context).textTheme.titleMedium,
            ),
          ),
          if (headerOptions != null) headerOptions!,
          Padding(
            padding: const EdgeInsets.fromLTRB(0, 8, 8, 8),
            child: headerIcon ?? _defaultHeaderIcon(context),
          ),
        ],
      ),
    );
  }

  Widget _defaultHeaderIcon(BuildContext context) {
    return InkWell(
      onTap: onClose ?? () => Navigator.of(context).pop(),
      child: Icon(
        Icons.close,
        size: PlatformInfo.isMobile ? 32 : 24,
      ),
    );
  }
}
