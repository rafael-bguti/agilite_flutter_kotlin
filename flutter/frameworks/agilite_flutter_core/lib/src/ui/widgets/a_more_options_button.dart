import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class AMoreOptionsButton extends StatelessWidget {
  final Widget Function(VoidCallback onTap) buttonBuilder;
  final List<MoreOption> options;
  const AMoreOptionsButton({
    required this.buttonBuilder,
    required this.options,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return MenuAnchor(
      builder: (BuildContext context, MenuController controller, Widget? child) {
        return buttonBuilder.call(() {
          if (controller.isOpen) {
            controller.close();
          } else {
            controller.open();
          }
        });
      },
      menuChildren: options.map((e) => e.build()).toList(),
    );
  }
}

class MoreOption {
  final double height;
  final Widget child;
  final void Function()? onTap;

  const MoreOption({
    required this.child,
    required void Function() this.onTap,
    this.height = 35,
  });

  MoreOption.icon({
    required Widget? icon,
    required Widget? label,
    required void Function() this.onTap,
    this.height = 35,
  }) : child = Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisSize: MainAxisSize.min,
          children: [
            icon ?? const SizedBox.shrink(),
            const SizedBox(width: 8),
            label ?? const SizedBox.shrink(),
          ],
        );

  MoreOption.divider()
      : child = const ADivider.lineOnly(),
        onTap = null,
        height = 8;

  Widget build() {
    return PopupMenuItem(
      height: height,
      onTap: onTap,
      enabled: onTap != null,
      child: child,
    );
  }
}
