import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/ui/layouts/a_drawer_menu.dart';
import 'package:flutter/material.dart';

class ADrawer extends StatelessWidget {
  final AFullLayoutController fullLayoutController;
  const ADrawer(
    this.fullLayoutController, {
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      mainAxisAlignment: MainAxisAlignment.start,
      children: [
        Center(
          child: Padding(
            padding: const EdgeInsets.symmetric(vertical: 24),
            child: coreStyle.getLogoWidget(LogoDestination.drawer, Brightness.dark),
          ),
        ),
        Center(
          child: Container(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
            decoration: BoxDecoration(
              color: primaryColor.withOpacity(0.1),
              borderRadius: BorderRadius.circular(90),
            ),
            child: ALoggedUserDropdown(
              fullLayoutController.drawerUserMenuController,
              onDrawer: true,
            ),
          ),
        ),
        const Expanded(
          child: Material(
            color: Colors.transparent,
            child: ADrawerMenu(),
          ),
        ),
      ],
    );
  }
}
