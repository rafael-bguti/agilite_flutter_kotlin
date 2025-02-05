import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudDataGroups extends StatelessWidget {
  final CrudController crudController;
  const CrudDataGroups(this.crudController, {super.key});

  @override
  Widget build(BuildContext context) {
    if (crudController.state.groups == null) {
      return const SizedBox();
    }

    return ATabHeader.builder(
      selectedIndex: crudController.state.selectedGroupIndex ?? 0,
      headerCount: crudController.state.groups!.length,
      contentBuilder: _buildStatusContext,
      onTabChanged: crudController.onGroupChanged,
    );
  }

  Widget _buildStatusContext(int index, bool selected, bool hover) {
    final textColor = !selected ? onBackgroundColor.withOpacity(0.6) : null;

    return ASpacingRow(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text(
          '${crudController.state.groups![index].qtd ?? ''}',
          style: textTheme?.labelLarge?.copyWith(
            fontSize: 18,
            color: textColor,
          ),
        ),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              crudController.state.groups![index].title,
              style: textTheme?.labelLarge?.copyWith(
                color: textColor,
              ),
            ),
          ],
        ),
      ],
    );
  }
}
