import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

const _folderOpenedBackgroundColor = Color(0xFF272C33);
const _unHoveredColor = Color(0xFF949799);
const _selectedItem = Color(0xFF438DB6);

class ADrawerMenu extends StatelessWidget {
  const ADrawerMenu({super.key});

  @override
  Widget build(BuildContext context) {
    return ListView.separated(
      padding: const EdgeInsets.all(8),
      itemBuilder: (_, itemIndex) {
        final item = fullLayoutFacade.menuItens[itemIndex];
        if (item.type == MenuItemType.header) {
          return _buildHeaderText(item);
        } else if (item.type == MenuItemType.group) {
          return _ADrawerMenuFolder(item);
        } else if (item.type == MenuItemType.item) {
          return _AMenuLink(item);
        } else {
          return const Placeholder();
        }
      },
      itemCount: fullLayoutFacade.menuItens.length,
      separatorBuilder: (_, __) => const SizedBox(height: 2),
    );
  }

  Widget _buildHeaderText(MenuItem item) {
    return Padding(
      padding: const EdgeInsets.only(left: 8, right: 8, top: 24, bottom: 8),
      child: Text(
        item.title?.toUpperCase() ?? '',
        style: textTheme!.titleSmall!.copyWith(
          color: _unHoveredColor,
          fontFamily: 'HeaderFont',
          fontWeight: FontWeight.bold,
          letterSpacing: 1.2,
        ),
      ),
    );
  }
}

class _AMenuLink extends StatefulWidget {
  final MenuItem item;

  const _AMenuLink(
    this.item,
  );

  @override
  State<_AMenuLink> createState() => _AMenuLinkState();
}

class _AMenuLinkState extends State<_AMenuLink> {
  bool _hovered = false;

  @override
  Widget build(BuildContext context) {
    return MouseRegion(
      cursor: SystemMouseCursors.click,
      onEnter: (_) {
        //TODO adicionar um LINK na WEB para que funcione com o Ctrl + Click
        setState(() {
          _hovered = true;
        });
      },
      onExit: (_) {
        setState(() {
          _hovered = false;
        });
      },
      child: GestureDetector(
        onTap: () => fullLayoutFacade.onMenuLinkTap(widget.item),
        child: Container(
          color: Colors.transparent,
          padding: const EdgeInsets.all(8.0),
          child: AnimatedBuilder(
            animation: fullLayoutFacade.controller.$runningItem,
            builder: (_, __) {
              final itemColor = fullLayoutFacade.itemSelectedOrIsGroupSelected(widget.item)
                  ? _selectedItem
                  : _highlight
                      ? Colors.white
                      : _unHoveredColor;

              return Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  Icon(
                    widget.item.icon ?? Icons.check_rounded,
                    color: itemColor,
                  ),
                  const SizedBox(width: 8),
                  Text(
                    widget.item.title ?? '',
                    style: textTheme!.bodyMedium!.copyWith(
                      color: itemColor,
                      fontWeight: FontWeight.bold,
                      fontSize: 15,
                    ),
                  ),
                  if (_isGroup) const Spacer(),
                  if (_isGroup)
                    AnimatedRotation(
                      key: ValueKey('ICON_${widget.item.id}'),
                      duration: Durations.short3,
                      turns: fullLayoutFacade.isGroupExpanded(widget.item) ? 0.25 : 0,
                      child: Icon(
                        Icons.keyboard_arrow_right,
                        color: _hovered ? Colors.white : _unHoveredColor,
                      ),
                    ),
                ],
              );
            },
          ),
        ),
      ),
    );
  }

  bool get _isGroup => widget.item.type == MenuItemType.group;

  bool get _highlight {
    if (_hovered) return true;

    if (_isGroup) {
      return fullLayoutFacade.isGroupSelected(widget.item);
    } else {
      return fullLayoutFacade.controller.$runningItem.value?.id == widget.item.id;
    }
  }
}

class _ADrawerMenuFolder extends StatelessWidget {
  final MenuItem item;
  const _ADrawerMenuFolder(this.item);

  @override
  Widget build(BuildContext context) {
    return Container(
      key: ValueKey('group_${item.id}'),
      child: AnimatedBuilder(
        animation: Listenable.merge([fullLayoutFacade.controller.$groupExpanded, fullLayoutFacade.controller.$runningItem]),
        builder: (_, __) {
          final isExpanded = fullLayoutFacade.isGroupExpanded(item);
          return Padding(
            padding: const EdgeInsets.only(right: 8.0),
            child: Container(
              decoration: BoxDecoration(
                color: isExpanded ? _folderOpenedBackgroundColor : Colors.transparent,
                borderRadius: const BorderRadius.only(
                  topRight: Radius.circular(16),
                  bottomRight: Radius.circular(16),
                ),
              ),
              // duration: Durations.medium3,
              // curve: Curves.easeInOut,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  _AMenuLink(
                    item,
                  ),
                  AnimatedSize(
                    key: ValueKey('EXPANDED_${item.id}'),
                    curve: Curves.easeIn,
                    duration: Durations.medium3,
                    child: isExpanded
                        ? Column(
                            children: _buildItens(),
                          )
                        : const SizedBox.shrink(),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  List<Widget> _buildItens() {
    return item.children!.map((child) {
      return _AMenuLink(child);
    }).toList();
  }
}
