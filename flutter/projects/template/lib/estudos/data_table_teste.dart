import 'package:flutter/material.dart';

void main() {
  runApp(MaterialApp(
    home: Scaffold(
      appBar: AppBar(
        title: Text('Tabela de Usuários com CheckBox'),
      ),
      body: UserTable(),
    ),
  ));
}

class UserTable extends StatefulWidget {
  @override
  _UserTableState createState() => _UserTableState();
}

class _UserTableState extends State<UserTable> {
  List<User> users = [
    User('Ollie Wallace', 'lorna.kirlin@nora.biz', 'Manager', '285-626-6050', '16 February 2019', false),
    User('Gilbert Barrett', 'paolo.zieme@gmail.com', 'Admin', '462-060-7408', '17 February 2019', false),
    User('Tony Parks', 'vida.glover@gmail.com', 'Admin', '169-769-4821', '18 February 2019', false),
    User('Billy Nunez', 'annabell.kris@yahoo.com', 'User', '239-721-3649', '19 February 2019', false),
  ];

  @override
  Widget build(BuildContext context) {
    return DataTable(
      columns: const <DataColumn>[
        DataColumn(label: Text('Name')),
        DataColumn(label: Text('Email')),
        DataColumn(label: Text('Tags')),
        DataColumn(label: Text('Phone')),
        DataColumn(label: Text('Added')),
      ],
      rows: users
          .map((user) => DataRow(
                  selected: user.selected,
                  onSelectChanged: (bool? value) {
                    setState(() {
                      user.selected = value!;
                    });
                  },
                  cells: [
                    DataCell(Text(user.name)),
                    DataCell(Text(user.email)),
                    DataCell(Chip(label: Text(user.tags))),
                    DataCell(Text(user.phone)),
                    DataCell(Text(user.added)),
                  ]))
          .toList(),
    );
  }
}

class User {
  final String name;
  final String email;
  final String tags;
  final String phone;
  final String added;
  bool selected;

  User(this.name, this.email, this.tags, this.phone, this.added, this.selected);
}
