import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

final configs = <_ButtonConfig>[
  const _ButtonConfig(label: "DEFAULT"),
  _ButtonConfig(label: "SUCCESS", backgroundColor: successColor, foregroundColor: onSuccessColor),
  _ButtonConfig(label: "PRIMARY", backgroundColor: primaryColor, foregroundColor: onPrimaryColor),
  _ButtonConfig(label: "WARNING", backgroundColor: warningColor, foregroundColor: onWarningColor),
  _ButtonConfig(label: "ERROR", backgroundColor: errorColor, foregroundColor: onErrorColor),
  const _ButtonConfig(label: "WHITE", backgroundColor: Colors.white, foregroundColor: Colors.black),
  const _ButtonConfig(label: "DARK", backgroundColor: Colors.black, foregroundColor: Colors.white),
];

class ButtonsScreen extends StatefulWidget {
  const ButtonsScreen({super.key});

  @override
  State<ButtonsScreen> createState() => _ButtonsScreenState();
}

class _ButtonsScreenState extends State<ButtonsScreen> {
  String _groupedValue = 'Opção 1';

  @override
  Widget build(BuildContext context) {
    return ATaskContainer(
      fluid: true,
      header: const AContainerHeader.text("Buttons"),
      child: ASpacingColumn(
        spacing: 32.0,
        children: [
          _Grid(
            label: "Elevated",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  ElevatedButton(
                    style: config.backgroundColor == null ? null : buildButtonStyle(config.backgroundColor!, config.foregroundColor!),
                    onPressed: () {},
                    child: Text(config.label),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Filled",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  FilledButton.tonal(
                    style: config.backgroundColor == null ? null : buildButtonStyle(config.backgroundColor!, config.foregroundColor!),
                    onPressed: () {},
                    child: Text(config.label),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Outlined",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  OutlinedButton(
                    style: config.backgroundColor == null ? null : buildOutlinedButtonStyle(config.backgroundColor!),
                    onPressed: () {},
                    child: Text(config.label),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Text",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  TextButton(
                    style: config.backgroundColor == null ? null : buildTextButtonStyle(config.backgroundColor!),
                    onPressed: () {},
                    child: Text(config.label),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Icon",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  IconButton(
                    style: config.backgroundColor == null ? null : buildOutlinedButtonStyle(config.backgroundColor!),
                    onPressed: () {},
                    icon: const Icon(Icons.health_and_safety_rounded),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Dropdown",
            child: Wrap(
              spacing: 8.0,
              runSpacing: 8.0,
              children: [
                for (final config in configs)
                  CustomDropdownButton(
                    label: Text(config.label),
                    style: config.backgroundColor == null ? buildButtonStyle(onPrimaryColor, primaryColor) : buildButtonStyle(config.backgroundColor!, config.foregroundColor!),
                  ),
              ],
            ),
          ),
          _Grid(
            label: "Grouped",
            child: Align(
              alignment: Alignment.centerLeft,
              child: IntrinsicWidth(
                child: SegmentedButton<String>(
                  segments: const <ButtonSegment<String>>[
                    ButtonSegment<String>(
                      value: 'Opção 1',
                      label: Text('Opção 1'),
                    ),
                    ButtonSegment<String>(
                      value: 'Opção 2',
                      label: Text('Opção 2'),
                    ),
                    ButtonSegment<String>(
                      value: 'Opção 3',
                      label: Text('Opção 3'),
                    ),
                  ],
                  selected: <String>{_groupedValue},
                  onSelectionChanged: (Set<String> newSelection) {
                    setState(() {
                      _groupedValue = newSelection.first;
                    });
                  },
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _ButtonConfig {
  final String label;
  final Color? backgroundColor;
  final Color? foregroundColor;
  const _ButtonConfig({
    required this.label,
    this.backgroundColor,
    this.foregroundColor,
  });
}

class _Grid extends StatelessWidget {
  final String label;
  final Widget child;
  const _Grid({
    required this.label,
    required this.child,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return AGrid.oneRow(
      areas: '4, 8',
      children: [
        ADivider.text(text: label),
        Padding(
          padding: const EdgeInsets.only(top: 32.0),
          child: child,
        ),
      ],
    );
  }
}

//TODO melhorar esse componente em sua própria classe para ser reutilizado
class CustomDropdownButton extends StatefulWidget {
  final Widget label;
  final ButtonStyle style;

  const CustomDropdownButton({
    required this.label,
    required this.style,
    super.key,
  });

  @override
  CustomDropdownButtonState createState() => CustomDropdownButtonState();
}

class CustomDropdownButtonState extends State<CustomDropdownButton> {
  final List<String> options = ['Opção 1', 'Opção 2', 'Opção 3'];

  void _showCustomMenu() {
    final RenderBox button = context.findRenderObject() as RenderBox;
    final RenderBox overlay = Overlay.of(context).context.findRenderObject() as RenderBox;
    final RelativeRect position = RelativeRect.fromRect(
      Rect.fromPoints(
        button.localToGlobal(Offset.zero, ancestor: overlay),
        button.localToGlobal(button.size.bottomRight(Offset.zero), ancestor: overlay),
      ),
      Offset.zero & overlay.size,
    );

    showMenu<String>(
      context: context,
      position: position,
      items: options.map((String option) {
        return PopupMenuItem<String>(
          value: option,
          child: Text(option),
        );
      }).toList(),
    ).then((value) {
      if (value != null) {
        // Lógica ao selecionar uma opção
        print('Opção selecionada: $value');
        // Se precisar atualizar o estado:
        setState(() {
          // Atualize o estado aqui
        });
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return FilledButton(
      onPressed: _showCustomMenu,
      style: widget.style,
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          widget.label,
          const SizedBox(width: 8),
          Icon(
            Icons.arrow_drop_down,
            color: widget.style.foregroundColor?.resolve({}),
          ),
        ],
      ),
    );
  }
}
