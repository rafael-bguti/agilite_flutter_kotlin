// lib/main.dart

import 'package:flutter/material.dart';

import 'user.dart';
import 'user_form_screen.dart';

void main() {
  runApp(UserCrudApp());
}

class UserCrudApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CRUD de Usuários',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: UserListScreen(),
    );
  }
}

class UserListScreen extends StatefulWidget {
  @override
  _UserListScreenState createState() => _UserListScreenState();
}

class _UserListScreenState extends State<UserListScreen> {
  List<User> users = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Lista de Usuários'),
      ),
      body: ListView.builder(
        itemCount: users.length,
        itemBuilder: (context, index) {
          final user = users[index];

          return ListTile(
            title: Text(user.name),
            subtitle: Text(user.email),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                // Botão de editar
                IconButton(
                  icon: Icon(Icons.edit),
                  onPressed: () async {
                    final editedUser = await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (context) => UserFormScreen(user: user),
                      ),
                    );

                    if (editedUser != null) {
                      setState(() {
                        users[index] = editedUser as User;
                      });
                    }
                  },
                ),
                // Botão de excluir
                IconButton(
                  icon: Icon(Icons.delete),
                  onPressed: () {
                    setState(() {
                      users.removeAt(index);
                    });
                  },
                ),
              ],
            ),
          );
        },
      ),
      // Botão flutuante para adicionar novo usuário
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add),
        onPressed: () async {
          final newUser = await Navigator.push(
            context,
            MaterialPageRoute(
              builder: (context) => UserFormScreen(),
            ),
          );

          if (newUser != null) {
            setState(() {
              users.add(newUser as User);
            });
          }
        },
      ),
    );
  }
}
