import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

enum HeaderType {
  underline,
  pills,
}

class ATabHeader extends StatefulWidget {
  final int headerCount;

  final void Function(int index) onTabChanged;
  final Widget Function(int index, bool selected, bool hover, Widget content)? wrapperBuilder;
  final Widget Function(int index, bool selected, bool hover)? contentBuilder;
  final List<String>? headerTexts;

  final int selectedIndex;
  final HeaderType type;

  const ATabHeader.builder({
    required this.headerCount,
    required this.contentBuilder,
    required this.onTabChanged,
    this.wrapperBuilder,
    this.selectedIndex = 0,
    this.type = HeaderType.underline,
    super.key,
  }) : headerTexts = null;

  const ATabHeader.text({
    required List<String> this.headerTexts,
    required this.onTabChanged,
    this.selectedIndex = 0,
    this.type = HeaderType.underline,
    super.key,
  })  : headerCount = headerTexts.length,
        wrapperBuilder = null,
        contentBuilder = null;

  @override
  State<ATabHeader> createState() => _ATabHeaderState();
}

class _ATabHeaderState extends State<ATabHeader> {
  late final int _headerCount = widget.headerCount;
  late int _selectedIndex = widget.selectedIndex;

  @override
  void didUpdateWidget(covariant ATabHeader oldWidget) {
    super.didUpdateWidget(oldWidget);

    if (oldWidget.selectedIndex != widget.selectedIndex) {
      _changeTab(widget.selectedIndex);
    }
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> hovers = [];
    for (int i = 0; i < _headerCount; i++) {
      hovers.add(
        AHover(
          builder: (hover) {
            return GestureDetector(
              onTap: () {
                _changeTab(i);
              },
              child: _buildWrapper(i, i == _selectedIndex, hover),
            );
          },
        ),
      );
    }

    final options = Wrap(
      spacing: 0,
      runSpacing: 0,
      children: hovers,
    );

    if (widget.type == HeaderType.pills) {
      return Container(
        padding: const EdgeInsets.only(bottom: 8.0),
        child: options,
      );
    } else {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisAlignment: MainAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: [
          options,
          const ADivider.lineOnly(),
        ],
      );
    }
  }

  Widget _buildWrapper(int index, bool selected, bool hover) {
    final Widget content = _buildContent(index, selected, hover);
    if (widget.wrapperBuilder != null) {
      return widget.wrapperBuilder!(index, selected, hover, content);
    } else {
      return Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
        //margin: const EdgeInsets.only(right: 8),
        decoration: _getDecorator(selected, hover),
        child: content,
      );
    }
  }

  Widget _buildContent(int index, bool selected, bool hover) {
    if (widget.contentBuilder != null) {
      return widget.contentBuilder!(index, selected, hover);
    } else {
      return Text(
        widget.headerTexts![index],
        style: textTheme!.labelLarge!.copyWith(
          color: selected && widget.type == HeaderType.pills ? onPrimaryColor : null,
        ),
      );
    }
  }

  BoxDecoration _getDecorator(bool selected, bool hover) {
    if (widget.type == HeaderType.pills) {
      final Color? color;
      if (selected) {
        color = primaryColor;
      } else if (hover) {
        color = primaryColor.withOpacity(0.2);
      } else {
        color = null;
      }

      return BoxDecoration(
        color: color,
        borderRadius: BorderRadius.circular(4), //TODO obter esse valor do CoreStyle que estará no Extensions do Theme
      );
    } else {
      final Color? color;
      if (selected) {
        color = null;
      } else if (hover) {
        color = primaryColor.withOpacity(0.2);
      } else {
        color = onBackgroundColor.withOpacity(0.05);
      }
      return BoxDecoration(
        color: color,
        border: Border(
          bottom: BorderSide(
            color: selected ? primaryColor : Colors.transparent,
            width: 4,
          ),
          right: BorderSide(
            color: onBackgroundColor.withOpacity(0.1),
            width: 1,
          ),
        ),
      );
    }
  }

  void _changeTab(int index) {
    widget.onTabChanged(index);
    setState(() {
      _selectedIndex = index;
    });
  }
}
