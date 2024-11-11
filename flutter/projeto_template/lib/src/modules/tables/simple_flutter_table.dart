import 'dart:math';

import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class SimpleFlutterTable extends StatefulWidget {
  const SimpleFlutterTable({super.key});

  @override
  _SimpleFlutterTableState createState() => _SimpleFlutterTableState();
}

class _SimpleFlutterTableState extends State<SimpleFlutterTable> {
  bool _sortAscending = true;
  int _sortColumnIndex = -1;

  List<User> users = [
    User("https://i.pravatar.cc/150?img=1", 'Ollie Wallace', 'lorna.kirlin@nora.biz', null, 'Manager', '285-626-6050', '16 February 2019', false),
    User("https://i.pravatar.cc/150?img=5", 'Gilbert Barrett', 'paolo.zieme@gmail.com', null, 'Admin', '462-060-7408', '17 February 2019', false),
    User("https://i.pravatar.cc/150?img=8", 'Tony Parks', 'vida.glover@gmail.com', Company('Frontend Matter Inc', 'Leuschkefurt'), 'Admin', '169-769-4821', '18 February 2019',
        true),
    User(
        "https://i.pravatar.cc/150?img=9", 'Billy Nunez', 'annabell.kris@yahoo.com', Company('Huma Huma Inc.', 'Huma Huma Inc.'), 'User', '239-721-3649', '19 February 2019', true),
  ];

  @override
  Widget build(BuildContext context) {
    return ACard(
      padding: const EdgeInsets.all(8),
      body: SizedBox(
        width: double.infinity,
        child: SingleChildScrollView(
          scrollDirection: Axis.horizontal,
          child: Padding(
            padding: const EdgeInsets.only(bottom: 12.0),
            child: DataTable(
              columns: <DataColumn>[
                DataColumn(
                  label: _buildHeader('Name', 0),
                  onSort: (columnIndex, ascending) {
                    setState(() {
                      if (_sortColumnIndex == columnIndex) {
                        _sortAscending = !_sortAscending;
                      } else {
                        _sortColumnIndex = columnIndex;
                        _sortAscending = true;
                      }
                      if (_sortAscending) {
                        users.sort((a, b) => a.name.compareTo(b.name));
                      } else {
                        users.sort((a, b) => b.name.compareTo(a.name));
                      }
                    });
                  },
                ),
                DataColumn(label: _buildHeader('Company', 1)),
                DataColumn(label: _buildHeader('Tags', 2)),
                DataColumn(label: _buildHeader('Phone', 3)),
                DataColumn(label: _buildHeader('Added', 4)),
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
                            DataCell(
                              ASpacingRow(
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
                              ),
                            ),
                            DataCell(
                              user.company == null
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
                                    ),
                            ),
                            DataCell(Chip(label: Text(user.tags))),
                            DataCell(Text(user.phone)),
                            DataCell(Text(user.added)),
                          ]))
                  .toList(),
            ),
          ),
        ),
      ),
    );
  }

  Widget _buildHeader(String label, int column) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Text(label),
        const SizedBox(width: 8),
        _getSortIcon(column),
      ],
    );
  }

  Widget _getSortIcon(int column) {
    if (_sortColumnIndex == column) {
      return _sortAscending
          ? const Icon(
              Icons.arrow_downward,
              size: 16,
            )
          : const Icon(
              Icons.arrow_upward,
              size: 16,
            );
    }
    return const SizedBox.shrink();
  }
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
}

class Company {
  final String name;
  final String location;

  Company(this.name, this.location);
}
