import 'dart:io' show File; // Para uso em mobile/desktop
import 'dart:typed_data' show Uint8List; // Para uso na Web

import 'package:file_picker/file_picker.dart';
import 'package:flutter/foundation.dart'; // kIsWeb para checar se é Web
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

void main() {
  runApp(const MyApp());
}

/// App principal
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Exemplo de Upload',
      theme: ThemeData(primarySwatch: Colors.blue),
      home: const UploadScreen(),
    );
  }
}

/// Tela de Upload com TextField e Botões
class UploadScreen extends StatefulWidget {
  const UploadScreen({super.key});

  @override
  State<UploadScreen> createState() => _UploadScreenState();
}

class _UploadScreenState extends State<UploadScreen> {
  /// Controller para o texto digitado pelo usuário
  final TextEditingController _textController = TextEditingController();

  /// Nome do arquivo selecionado (para exibir na tela)
  String? _fileName;

  /// Armazena o arquivo selecionado (Mobile/Desktop)
  File? _selectedFile;

  /// Armazena o conteúdo do arquivo em bytes (Web)
  Uint8List? _selectedFileBytes;

  /// Função para abrir o seletor de arquivos
  Future<void> _pickFile() async {
    final result = await FilePicker.platform.pickFiles();
    if (result == null) {
      // Usuário cancelou
      return;
    }

    if (kIsWeb) {
      // Web: só temos bytes, não há "path" físico
      setState(() {
        _selectedFileBytes = result.files.single.bytes;
        _fileName = result.files.single.name;
      });
    } else {
      // Mobile/desktop: temos um path real
      setState(() {
        _selectedFile = File(result.files.single.path!);
        _fileName = result.files.single.name;
      });
    }
  }

  /// Função para enviar o arquivo e o texto adicional para o servidor
  Future<void> _uploadFile() async {
    if (_fileName == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Nenhum arquivo selecionado!')),
      );
      return;
    }

    // Você pode enviar o texto do _textController.text ao servidor também
    // conforme sua necessidade. Neste exemplo, enviaremos apenas o arquivo.

    final uri = Uri.parse('https://seu-servidor.com/upload'); // Ajuste aqui!

    try {
      if (kIsWeb) {
        // Para Web: enviar os bytes do arquivo
        final request = http.MultipartRequest('POST', uri);

        final multipartFile = http.MultipartFile.fromBytes(
          'file', // nome do campo esperado pelo servidor
          _selectedFileBytes!,
          filename: _fileName,
        );

        request.files.add(multipartFile);

        final response = await request.send();
        if (response.statusCode == 200) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Upload (Web) realizado com sucesso!')),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Falha no upload: ${response.statusCode}')),
          );
        }
      } else {
        // Mobile/desktop: enviar via path
        final file = _selectedFile!;
        final request = http.MultipartRequest('POST', uri);

        request.files.add(
          await http.MultipartFile.fromPath('file', file.path),
        );

        final response = await request.send();
        if (response.statusCode == 200) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Upload (Mobile/Desktop) realizado com sucesso!')),
          );
        } else {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text('Falha no upload: ${response.statusCode}')),
          );
        }
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Ocorreu um erro: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Exemplo de Upload em Flutter'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Campo para texto adicional
            TextField(
              controller: _textController,
              decoration: const InputDecoration(
                labelText: 'Texto adicional',
                hintText: 'Digite algo que será enviado junto...',
              ),
            ),
            const SizedBox(height: 24),

            // "Campo de arquivo": exibindo o nome + botão "Selecionar arquivo"
            Row(
              children: [
                // Exibe o nome do arquivo ou "Nenhum selecionado"
                Expanded(
                  child: Text(
                    _fileName ?? 'Nenhum arquivo selecionado',
                    overflow: TextOverflow.ellipsis,
                  ),
                ),
                const SizedBox(width: 8),
                ElevatedButton(
                  onPressed: _pickFile,
                  child: const Text('Selecionar arquivo'),
                ),
              ],
            ),
            const SizedBox(height: 24),

            // Botão de enviar
            ElevatedButton(
              onPressed: _uploadFile,
              child: const Text('Enviar para o servidor'),
            ),
          ],
        ),
      ),
    );
  }
}
