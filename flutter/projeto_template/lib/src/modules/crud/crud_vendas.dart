import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class CrudVendas extends StatefulWidget {
  const CrudVendas({super.key});

  @override
  State<CrudVendas> createState() => _CrudVendasState();
}

class _CrudVendasState extends State<CrudVendas> {
  final ValueNotifier<int> _$groupSelectedTab = ValueNotifier(0);

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        headerLabel: "Vendas",
        headerOptions: ElevatedButton.icon(
          onPressed: () {},
          icon: const Icon(Icons.add),
          label: const Text("Nova Nota Fiscal"),
          style: successButtonStyle,
        ),
        child: ASpacingColumn(
          spacing: 16,
          children: [
            const PainelDeFiltros(),
            ACard(
              padding: EdgeInsets.zero,
              child: Column(
                children: [
                  const TabGroups(),
                  const UsersSpread(),
                  Padding(
                    padding: const EdgeInsets.only(top: 12),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: [
                        Padding(
                          padding: const EdgeInsets.symmetric(horizontal: 8.0),
                          child: APaginator(
                            state: PaginatorState(pageSize: 50, currentPage: 2, totalRecords: 1256),
                            onPageSizeChange: (pgSize) {},
                            onPageChange: (page) {},
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class TabGroups extends StatelessWidget {
  const TabGroups({super.key});

  @override
  Widget build(BuildContext context) {
    final localGroups = _groups;
    return ATabHeader.builder(
      headerCount: localGroups.length,
      contentBuilder: _buildStatusContext,
      onTabChanged: (index) {},
    );
  }

  Widget _buildStatusContext(int index, bool selected, bool hover) {
    final textColor = !selected ? onBackgroundColor.withOpacity(0.6) : null;

    return ASpacingRow(
      mainAxisSize: MainAxisSize.min,
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text('${_groups[index].qtd}', style: textTheme?.labelLarge?.copyWith(fontSize: 18, color: textColor)),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(_groups[index].title, style: textTheme?.labelLarge?.copyWith(color: textColor)),
          ],
        ),
      ],
    );
  }

  List<CrudStatusGroup> get _groups => [
        CrudStatusGroup(775, "Todos"),
        CrudStatusGroup(50, "Em aberto"),
        CrudStatusGroup(125, "Aprovado"),
        CrudStatusGroup(22, "Enviando"),
        CrudStatusGroup(8, "Finalizado"),
      ];
}

class PainelDeFiltros extends StatelessWidget {
  const PainelDeFiltros({super.key});

  @override
  Widget build(BuildContext context) {
    return AGrid.rows(
      //TODO adicionar um form
      crossAxisAlignment: WrapCrossAlignment.center,
      [
        AGridRow("3-4, *", [
          ASearchField(
            //TODO alterar o codigo do search para que ele gere valor no form igual aos outros inputs
            onSearchChanged: (value) {
              print('Buscar por $value');
            },
          ),
          Row(
            children: [
              OutlinedButton.icon(
                style: buildOutlinedButtonStyle(primaryColor),
                onPressed: () {},
                label: const Text("Data de emissão"),
                icon: const Icon(Icons.calendar_today),
              ),
              const Spacer(),
              OutlinedButton.icon(
                style: buildOutlinedButtonStyle(primaryColor),
                onPressed: () {},
                label: const Text("Mais filtros"),
                icon: const Icon(Icons.filter_alt_outlined),
              )
            ],
          ),
        ]),
      ],
    );
  }
}

class CrudStatusGroup {
  final int qtd;
  final String title;
  final String? subtitle;

  CrudStatusGroup(this.qtd, this.title, {this.subtitle});
}

//-----------------------------Teste-----------------------------
//-----------------------------Teste-----------------------------
//-----------------------------Teste-----------------------------
//-----------------------------Teste-----------------------------

List<User> users = [
  User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
  User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
  User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019', true),
  User("https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
  User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
  User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
  User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019', true),
  User("https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
  User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
  User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
  User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019', true),
  User("https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
  User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
  User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
  User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019', true),
  User("https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
];

class UsersSpread extends StatefulWidget {
  const UsersSpread({super.key});

  @override
  State<UsersSpread> createState() => _UsersSpreadState();
}

class _UsersSpreadState extends State<UsersSpread> {
  final formController = AFormController();

  @override
  void initState() {
    super.initState();
    runOnNextBuild(() {
      (formController.getControllerByName("sprUsers") as ASpreadController?)?.fillFromList(users.map((e) => e.toJson()).toList());
    });
  }

  @override
  Widget build(BuildContext context) {
    return AForm(
      formController,
      child: SizedBox(
        height: 450,
        child: ASpread.columns(
          'sprUsers',
          [
            AColumnString("name", 'Nome'),
            AColumnString("phone", 'Phone'),
            AColumnString("tags", 'Tags'),
          ],
          onRowTap: (index) {
            print('Linha $index');
          },
          selectColumnName: 'selected',
          selectPanelWidget: TextButton.icon(
            onPressed: () {},
            style: TextButton.styleFrom(foregroundColor: Colors.red),
            icon: const Icon(Icons.delete_outline_outlined),
            label: const Text("Excluir selecionados"),
          ),
        ),
      ),
    );
  }
}

class User {
  final String image;
  final String name;
  final String email;
  final Company? company;
  final String tags;
  final String phone;
  final String added;
  bool selected;

  User(this.image, this.name, this.email, this.company, this.tags, this.phone, this.added, this.selected);

  Map<String, dynamic> toJson() => {
        'image': image,
        'name': name,
        'email': email,
        'company': company?.toJson(),
        'tags': tags,
        'phone': phone,
        'added': added,
        'selected': selected,
      };
}

class Company {
  final String name;
  final String location;

  Company(this.name, this.location);

  Map<String, dynamic> toJson() => {
        'name': name,
        'location': location,
      };
}

class CompanyAvatar extends StatelessWidget {
  final String userName;
  final double size;

  const CompanyAvatar({super.key, required this.userName, this.size = 40.0});

  @override
  Widget build(BuildContext context) {
    return Container(
      width: size,
      height: size,
      decoration: BoxDecoration(
        color: _randomColor(),
        borderRadius: BorderRadius.circular(4.0), // Ajuste para mais ou menos arredondado
      ),
      margin: const EdgeInsets.only(right: 8),
      child: Center(
        child: Text(
          _getInitials(userName),
          style: const TextStyle(
            fontSize: 13,
            fontWeight: FontWeight.bold,
            color: Colors.white,
          ),
        ),
      ),
    );
  }

  // Função para extrair as duas primeiras letras do nome
  String _getInitials(String name) {
    List<String> names = name.split(' ');
    String initials = names[0][0];
    if (names.length > 1) {
      initials += names[1][0];
    }
    return initials.toUpperCase();
  }

  // Função para gerar uma cor aleatória
  Color _randomColor() {
    Random random = Random();
    return Color.fromRGBO(
      random.nextInt(256),
      random.nextInt(256),
      random.nextInt(256),
      1,
    );
  }
}
