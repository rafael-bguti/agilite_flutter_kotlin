import 'package:agilite_flutter_core/core.dart';
import 'package:agilite_flutter_core/src/ui/layouts/a_drawer.dart';
import 'package:flutter/material.dart';

const kMinWidthMenuFixed = 1280.0;
const _kTopbarHeight = 64.0;
const kSidebarWidth = 255.0;

FullLayoutFacade fullLayoutFacade = FullLayoutFacade();

class AFullLayout extends StatefulWidget {
  final Widget child;
  const AFullLayout({
    required this.child,
    super.key,
  });

  @override
  State<AFullLayout> createState() => _AFullLayoutState();
}

class _AFullLayoutState extends State<AFullLayout> {
  final AFullLayoutController menuController = AFullLayoutController();

  @override
  void initState() {
    super.initState();
    fullLayoutFacade.initController(menuController);
  }

  @override
  void dispose() {
    menuController._dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      extendBody: true,
      body: LayoutBuilder(builder: (context, size) {
        menuController.layout = size.maxWidth >= kMinWidthMenuFixed ? Layout.fixed : Layout.floating;
        return Stack(
          children: [
            AnimatedPadding(
              key: const Key("DesktopBodyPadding"),
              duration: kThemeAnimationDuration,
              padding: EdgeInsets.only(left: size.maxWidth < kMinWidthMenuFixed ? 0 : kSidebarWidth),
              child: Column(
                children: [
                  ATopBar(
                    menuController: menuController,
                  ),
                  const SizedBox(height: 16),
                  Expanded(child: widget.child),
                ],
              ),
            ),
            AnimatedBuilder(
              animation: menuController.$sideMenuVisibleValue,
              builder: (_, __) {
                if (menuController.$sideMenuVisibleValue.value) {
                  return GestureDetector(
                    onTap: () {
                      menuController.$sideMenuVisibleValue.value = false;
                    },
                    child: Container(
                      color: Colors.black.withOpacity(0.5),
                    ),
                  );
                }
                return const SizedBox.shrink();
              },
            ),
            AnimatedBuilder(
              animation: menuController.$sideMenuVisibleValue,
              builder: (_, __) {
                final leftMenuPosition = menuController.$sideMenuVisibleValue.value ? 0.0 : -kSidebarWidth;
                return AnimatedPositioned(
                  key: const Key('DesktopSideBar'),
                  duration: kThemeAnimationDuration,
                  curve: Curves.fastOutSlowIn,
                  top: 0,
                  bottom: 0,
                  left: size.maxWidth < kMinWidthMenuFixed ? leftMenuPosition : 0,
                  child: ASideBar(menuController),
                );
              },
            ),
          ],
        );
      }),
    );
  }
}

class ASideBar extends StatelessWidget {
  final AFullLayoutController menuController;
  const ASideBar(this.menuController, {super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTextStyle(
      style: textTheme!.bodyMedium!.copyWith(color: coreStyleColors?.onSideBarColor),
      child: Container(
        width: kSidebarWidth,
        height: double.infinity,
        color: coreStyleColors?.sideBarColor,
        child: ADrawer(menuController),
      ),
    );
  }
}

class ATopBar extends StatelessWidget {
  final AFullLayoutController menuController;
  const ATopBar({
    required this.menuController,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    int alertMsgs = 6; //TODO - Implementar
    int emailMsgs = 0; //TODO - Implementar

    Widget leftWidget = menuController.layout == Layout.floating
        ? Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              IconButton(
                icon: const Icon(Icons.menu, size: 35),
                color: coreStyleColors?.onAppBarColor,
                onPressed: () {
                  menuController.$sideMenuVisibleValue.value = !menuController.$sideMenuVisibleValue.value;
                },
              ),
              const SizedBox(width: 8),
              coreStyle.getLogoWidget(LogoDestination.appBar, Theme.of(context).brightness),
            ],
          )
        : const SizedBox.shrink();

    return Container(
      decoration: BoxDecoration(
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.4),
            blurRadius: 16,
            offset: const Offset(0, 0),
            spreadRadius: -6,
          ),
        ],
        color: coreStyleColors?.appBarColor,
      ),
      height: _kTopbarHeight,
      child: Padding(
        padding: const EdgeInsets.symmetric(horizontal: 8.0),
        child: Row(
          mainAxisSize: MainAxisSize.max,
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.start,
          children: [
            leftWidget,
            const Spacer(),
            AAlertBadge(Icons.notifications_sharp, alertMsgs),
            const SizedBox(width: 8),
            AAlertBadge(Icons.email, emailMsgs),
            const SizedBox(width: 8),
            ALoggedUserDropdown(
              menuController.topBarUserMenuController,
            ),
            const SizedBox(width: 8),
          ],
        ),
      ),
    );
  }
}

class AAlertBadge extends StatelessWidget {
  final int count;
  final IconData icon;
  const AAlertBadge(this.icon, this.count, {super.key});

  @override
  Widget build(BuildContext context) {
    return Badge(
      alignment: Alignment.center,
      offset: const Offset(6, -14),
      isLabelVisible: count > 0,
      label: Text(
        count.toString(),
        style: const TextStyle(
          fontSize: 9,
          color: Colors.white,
          fontWeight: FontWeight.bold,
        ),
      ),
      child: IconButton(
        onPressed: () {
          print('TODO - Open Alert'); // TODO Implementar
        },
        icon: Icon(
          icon,
          size: 28,
          color: onBackgroundColor.withOpacity(0.7),
        ),
      ),
    );
  }
}

class ALoggedUserDropdown extends StatelessWidget {
  final MenuController _menuController;
  final bool onDrawer;
  const ALoggedUserDropdown(
    this._menuController, {
    this.onDrawer = false,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    if (ScreenSize(context).isPhone()) return const SizedBox.shrink();

    return MenuAnchor(
      controller: _menuController,
      menuChildren: [
        SizedBox(
          width: 300,
          child: Material(
              color: backgroundColor,
              elevation: 18,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  ListTile(
                    leading: Icon(Icons.person, color: onBackgroundColor),
                    title: Text('Perfil', style: TextStyle(color: onBackgroundColor)),
                    onTap: () => print('Perfil'),
                  ),
                  ListTile(
                    leading: Icon(Icons.settings, color: onBackgroundColor),
                    title: Text('Configurações', style: TextStyle(color: onBackgroundColor)),
                    onTap: () => print('Configurações'),
                  ),
                  //TODO remover o Switch do Tema daqui e adicionar em uma página de configuração que não tenha nenhum const para que não fique travado o tema ao alterar
                  _ThemeSwitcherTile(isDarkMode: Theme.of(context).brightness == Brightness.dark),
                  const Divider(),
                  ListTile(
                    leading: Icon(Icons.exit_to_app, color: errorColor),
                    title: Text('Sair', style: TextStyle(color: errorColor)),
                    onTap: () {
                      _menuController.close();
                      coreEventBus.fire(SysEventOnExitButtonTap());
                    },
                  ),
                ],
              )),
        ),
      ],
      child: InkWell(
        onTap: () {
          _menuController.open();
        },
        child: Row(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            const CircleAvatar(
              radius: 16,
              backgroundImage: NetworkImage('https://huma.demo.frontendmatter.com/assets/images/people/50/guy-3.jpg'),
            ),
            const SizedBox(width: 8),
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Usuário Logado',
                  style: textTheme!.bodyMedium!.copyWith(
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                    color: onDrawer ? coreStyleColors!.onSideBarColor : onBackgroundColor,
                  ),
                ),
                Text(
                  'Gerente de Projetos',
                  style: textTheme!.bodyMedium!.copyWith(
                    fontSize: 13,
                    fontWeight: FontWeight.w300,
                    color: onDrawer ? coreStyleColors!.onSideBarColor : onBackgroundColor,
                  ),
                ),
              ],
            ),
            Icon(
              Icons.arrow_drop_down,
              color: onDrawer ? coreStyleColors!.onSideBarColor : onBackgroundColor,
            ),
          ],
        ),
      ),
    );
  }
}

enum Layout { fixed, floating }

class _ThemeSwitcherTile extends StatelessWidget {
  final bool isDarkMode;

  const _ThemeSwitcherTile({
    required this.isDarkMode,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(isDarkMode ? Icons.dark_mode : Icons.light_mode),
      title: Text('Modo ${isDarkMode ? 'Escuro' : 'Claro'}'),
      trailing: Switch(
        value: isDarkMode,
        onChanged: (_) => onChanged(context),
      ),
      onTap: () {
        onChanged(context);
      },
    );
  }

  void onChanged(BuildContext context) {
    if (themeNotifier.value == ThemeMode.system) {
      final brightness = MediaQuery.of(context).platformBrightness;
      themeNotifier.value = brightness == Brightness.dark ? ThemeMode.light : ThemeMode.dark;
    } else {
      themeNotifier.value = themeNotifier.value == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
    }

    StyleHelper.onChangeTheme();
  }
}

class AFullLayoutController {
  Layout? layout;
  final $sideMenuVisibleValue = ValueNotifier<bool>(false);

  //User menu Controllers
  final MenuController topBarUserMenuController = MenuController();
  final MenuController drawerUserMenuController = MenuController();

  //Sidebar menu
  final $groupExpanded = ValueNotifier<String>('');
  final $runningItem = ValueNotifier<MenuItem?>(null);

  void _dispose() {
    $sideMenuVisibleValue.dispose();
    $groupExpanded.dispose();
    $runningItem.dispose();
  }
}

class FullLayoutFacade {
  AFullLayoutController? _controller;
  final List<MenuItem> menuItens = [];
  final Map<String, MenuItem> _itensByRoute = {};

  void setMenuItems(List<MenuItem> menuItens) {
    this.menuItens.clear();
    this.menuItens.addAll(menuItens);

    _itensByRoute.clear();
    for (final item in menuItens) {
      _populateMapByRoute(item);
    }
  }

  AFullLayoutController get controller => _controller!;

  void initController(AFullLayoutController controller) {
    _controller = controller;
  }

  void selectItem(MenuItem item) {
    if (item.id == _controller?.$runningItem.value?.id) return;

    if (controller.layout == Layout.floating) {
      controller.$sideMenuVisibleValue.value = false;
    }

    if (item.parentId != null) {
      expandMenuGroup(item.parentId!);
    }
    _controller?.$runningItem.value = item;
  }

  void expandMenuGroup(String menuId) {
    _controller?.$groupExpanded.value = menuId;
  }

  void toggleMenuGroup(String menuId) {
    if (_controller?.$groupExpanded.value == menuId) {
      _controller?.$groupExpanded.value = '';
    } else {
      _controller?.$groupExpanded.value = menuId;
    }
  }

  void selectItemByRoute(String route) {
    final item = _itensByRoute[route];
    if (item != null) {
      selectItem(item);
    }
  }

  void onMenuLinkTap(MenuItem item) {
    if (item.type == MenuItemType.group) {
      toggleMenuGroup(item.id);
    } else if (item.route != null) {
      selectItem(item);
      ANavigator.replace(item.route!);
    }
  }

  void selectMenuItemAndNavigationReplace(String route) {
    selectItemByRoute(route);
    ANavigator.replace(route);
  }

  bool isGroupExpanded(MenuItem item) {
    return item.type == MenuItemType.group && (controller.$groupExpanded.value == item.id);
  }

  bool isGroupSelected(MenuItem item) {
    return item.type == MenuItemType.group && (controller.$groupExpanded.value == item.id || controller.$runningItem.value?.parentId == item.id);
  }

  bool itemSelectedOrIsGroupSelected(MenuItem item) {
    if (item.type == MenuItemType.group) {
      return controller.$runningItem.value?.parentId == item.id;
    } else {
      return controller.$runningItem.value?.id == item.id;
    }
  }

  void _populateMapByRoute(MenuItem item) {
    if (item.route != null) {
      _itensByRoute[item.route!] = item;
    }
    if (item.children != null) {
      for (final child in item.children!) {
        _populateMapByRoute(child);
      }
    }
  }
}
