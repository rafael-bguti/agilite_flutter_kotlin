// lib/user_form_screen.dart

import 'package:flutter/material.dart';

import 'user.dart';

class UserFormScreen extends StatefulWidget {
  final User? user;

  UserFormScreen({this.user});

  @override
  _UserFormScreenState createState() => _UserFormScreenState();
}

class _UserFormScreenState extends State<UserFormScreen> {
  final _formKey = GlobalKey<FormState>();
  final _uuid = "123";

  late String _name;
  late String _email;
  late String _password;

  @override
  void initState() {
    super.initState();
    _name = widget.user?.name ?? '';
    _email = widget.user?.email ?? '';
    _password = widget.user?.password ?? '';
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.user == null ? 'Adicionar Usuário' : 'Editar Usuário'),
      ),
      body: Padding(
        padding: EdgeInsets.all(16.0),
        child: Form(
          key: _formKey,
          autovalidateMode: AutovalidateMode.onUserInteraction,
          child: Column(
            children: [
              // Campo Nome
              TextFormField(
                initialValue: _name,
                decoration: InputDecoration(labelText: 'Nome'),
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira o nome';
                  }
                  return null;
                },
                onSaved: (value) {
                  _name = value!;
                },
              ),
              // Campo Email
              TextFormField(
                initialValue: _email,
                decoration: InputDecoration(labelText: 'Email'),
                keyboardType: TextInputType.emailAddress,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira o email';
                  } else if (!RegExp(r'^[^@]+@[^@]+\.[^@]+').hasMatch(value)) {
                    return 'Por favor, insira um email válido';
                  }
                  return null;
                },
                onSaved: (value) {
                  _email = value!;
                },
              ),
              // Campo Senha
              TextFormField(
                initialValue: _password,
                decoration: InputDecoration(labelText: 'Senha'),
                obscureText: true,
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira a senha';
                  } else if (value.length < 6) {
                    return 'A senha deve ter pelo menos 6 caracteres';
                  }
                  return null;
                },
                onSaved: (value) {
                  _password = value!;
                },
              ),
              SizedBox(height: 20),
              ElevatedButton(
                child: Text('Salvar'),
                onPressed: () {
                  if (_formKey.currentState!.validate()) {
                    _formKey.currentState!.save();

                    final user = User(
                      id: widget.user?.id ?? _uuid,
                      name: _name,
                      email: _email,
                      password: _password,
                    );

                    Navigator.pop(context, user);
                  }
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
