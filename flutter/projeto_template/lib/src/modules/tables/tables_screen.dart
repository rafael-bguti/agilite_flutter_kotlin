import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';
import 'package:projeto_estudo/src/modules/tables/simple_flutter_table.dart';

class TablesScreen extends StatelessWidget {
  const TablesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: AContainer(
        header: const AContainerHeader.text("Tables / Spread"),
        child: ASpacingColumn(
          crossAxisAlignment: CrossAxisAlignment.start,
          spacing: 32,
          children: const [
            ADivider.text(text: "Spread read only - Custom Cell"),
            AText("Linha 2 está com tooltip e a linha 3 está com a cor de fundo alterada"),
            UsersSpread(),
            ADivider.text(text: "Simple Spread - With more detail"),
            SimpleSpreadEditavel(),
            ADivider.text(text: "Simple Flutter Table"),
            SimpleFlutterTable(),
          ],
        ),
      ),
    );
  }
}

class UsersSpread extends StatefulWidget {
  const UsersSpread({super.key});

  @override
  State<UsersSpread> createState() => _UsersSpreadState();
}

class _UsersSpreadState extends State<UsersSpread> {
  final formController = FormController();

  @override
  void initState() {
    super.initState();
    runOnNextBuild(() {
      (formController.getControllerByName("sprUsers") as SpreadController?)?.fillFromList(users.map((e) => e.toJson()).toList());
    });
  }

  @override
  Widget build(BuildContext context) {
    return ACard(
      child: AForm(
        formController,
        child: SizedBox(
          height: 350,
          child: ASpread.columns(
            readOnly: true,
            selectColumnName: 'selected',
            moreDetail: SpreadMoreDetail(),
            'sprUsers',
            [
              AColumnReadOnly("nome", 'Name', renderWidgetBuilder: _renderName).widthChar(35),
              AColumnReadOnly("company", 'Company', renderWidgetBuilder: _renderCompany).widthChar(75),
              AColumnReadOnly("tags", 'Tags', renderWidgetBuilder: _renderTags).widthChar(50),
            ],
            rowBuilder: _rowBuilder,
          ),
        ),
      ),
    );
  }

  Widget _rowBuilder(BuildContext context, SpreadController controller, int rowIndex, Widget rowContent) {
    bool isSelected = controller.isRowSelected(rowIndex);

    final wrapper = Container(
      key: ValueKey('row$rowIndex'),
      height: controller.rowHeight,
      decoration:
          rowIndex == 2 ? tableRowDecoration.copyWith(color: Colors.red.withOpacity(0.4)) : tableRowDecoration.copyWith(color: isSelected ? primaryColor.withOpacity(0.2) : null),
      child: rowContent,
    );

    if (rowIndex == 1) {
      return Tooltip(
        message: "This is a tooltip",
        child: wrapper,
      );
    }

    return wrapper;
  }

  Widget _renderName(BuildContext context, SpreadController spreadController, int row, bool isFocused) {
    final user = users[row];
    return ASpacingRow(
      children: [
        CircleAvatar(
          radius: 16,
          backgroundImage: NetworkImage(user.image),
        ),
        Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              user.name,
              style: textTheme?.labelLarge,
            ),
            Text(
              user.email,
              style: textTheme?.labelMedium?.copyWith(fontWeight: FontWeight.normal),
            ),
          ],
        ),
      ],
    );
  }

  Widget _renderCompany(BuildContext context, SpreadController spreadController, int row, bool isFocused) {
    final user = users[row];
    return user.company == null
        ? const Text('')
        : Row(
            mainAxisSize: MainAxisSize.min,
            children: [
              CompanyAvatar(userName: user.company!.name),
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text(user.company!.name, style: textTheme?.labelLarge),
                  Text(user.company!.location, style: textTheme?.labelMedium?.copyWith(fontWeight: FontWeight.normal)),
                ],
              ),
            ],
          );
  }

  Widget _renderTags(BuildContext context, SpreadController spreadController, int row, bool isFocused) {
    final user = users[row];
    return Chip(label: Text(user.tags));
  }
}

class SimpleSpreadEditavel extends StatefulWidget {
  const SimpleSpreadEditavel({super.key});

  @override
  State<SimpleSpreadEditavel> createState() => _SimpleSpreadEditavelState();
}

class _SimpleSpreadEditavelState extends State<SimpleSpreadEditavel> {
  final formController = FormController();

  @override
  void initState() {
    super.initState();
    runOnNextBuild(() {
      (formController.getControllerByName("sprNaoEditavel") as SpreadController?)?.fillFromList([
        {
          "nome": "João",
          "idade": 30,
        },
        {
          "nome": "Maria",
          "idade": 32,
        }
      ]);
    });
  }

  @override
  void dispose() {
    formController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return ACard(
      child: AForm(
        formController,
        child: ASpread.columns(
          moreDetail: SpreadMoreDetail(),
          disableScroll: true,
          'sprNaoEditavel',
          [
            AColumnString("nome", 'Nome'),
            AColumnInt("idade", 'Idade'),
          ],
        ),
      ),
    );
  }
}

List<User> users = [
  User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
  User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
  User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019', true),
  User("https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
];

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
