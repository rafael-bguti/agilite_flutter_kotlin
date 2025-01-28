import 'package:agilite_flutter_core/core.dart';
import 'package:flutter/material.dart';

class FormsScreen extends StatelessWidget {
  const FormsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return ATaskContainer(
      header: const AContainerHeader.text("Forms"),
      child: ASpacingColumn(
        spacing: 16,
        children: [
          AGrid(
            areas: const ['4, 8'],
            children: [
              Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  const ADivider.text(text: "Simple form with Validation"),
                  AText(
                    "Provide valuable, actionable feedback to your users with HTML5 form validation – available in all our supported browsers.",
                    style: moreDetailTextStyle,
                  ),
                ],
              ),
              const Padding(
                padding: EdgeInsets.only(top: 24.0),
                child: SimpleForm(),
              ),
            ],
          ),
          AGrid(
            areas: const ['4, 8'],
            children: const [
              ADivider.text(text: "Autocomplete/Combo"),
              Padding(
                padding: EdgeInsets.only(top: 24.0),
                child: Autocompletes(),
              ),
            ],
          ),
        ],
      ),
    );
  }
}

class Autocompletes extends StatefulWidget {
  const Autocompletes({super.key});

  @override
  State<Autocompletes> createState() => _AutocompletesState();
}

class _AutocompletesState extends State<Autocompletes> {
  final formController = FormController();

  @override
  Widget build(BuildContext context) {
    return AForm(
      formController,
      child: ASpacingColumn(
        spacing: 16,
        children: [
          AComboField("comboFixo", labelText: "Combobox Fixo", options: [
            LocalOption<String>("1", "Opção 1"),
            LocalOption<String>("2", "Opção 2"),
            LocalOption<String>("3", "Opção 3"),
          ]),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AAutocompleteField.options("autocompleteFixo", labelText: "Autocomplete Fixo", options: [
                LocalOption<String>("1", "Opção 1"),
                LocalOption<String>("2", "Opção 2"),
                LocalOption<String>("3", "Opção 3"),
              ]),
              AText("No Autocomplete com Options, o sistema exibe as opções paginado", style: moreDetailTextStyle),
            ],
          ),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AAutocompleteField.combo("autocompleteCombo", labelText: "Autocomplete Combo", options: [
                LocalOption<String>("1", "Opção 1"),
                LocalOption<String>("2", "Opção 2"),
                LocalOption<String>("3", "Opção 3"),
              ]),
              AText("No Autocomplete Combo, o sistema exibe todas as opções sem página", style: moreDetailTextStyle),
            ],
          ),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              AAutocompleteField.combo(
                "autocompleteCustomField",
                labelText: "Autocomplete Custom Field",
                options: [
                  LocalOption<String>("1", "João", moreDetails: {"image": "https://i.pravatar.cc/150?img=1", "empresa": "Company A"}),
                  LocalOption<String>("2", "José", moreDetails: {"image": "https://i.pravatar.cc/150?img=2", "empresa": "Company B"}),
                  LocalOption<String>("3", "Maria", moreDetails: {"image": "https://i.pravatar.cc/150?img=3", "empresa": "Company C"}),
                ],
                listItemBuilder: _buildCustomListItem,
              ),
              AText("Custom autocomplete Field", style: moreDetailTextStyle),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildCustomListItem(Option<String> item) {
    final moreDetails = item.getMoreDetails();
    return ASpacingRow(
      children: [
        CircleAvatar(
          radius: 16,
          backgroundImage: NetworkImage(moreDetails!["image"] as String),
        ),
        Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            AText(item.label),
            AText(moreDetails["empresa"] as String, style: moreDetailTextStyle),
          ],
        ),
      ],
    );
  }
}

class SimpleForm extends StatefulWidget {
  const SimpleForm({super.key});

  @override
  State<SimpleForm> createState() => _SimpleFormState();
}

class _SimpleFormState extends State<SimpleForm> {
  final formController = FormController();
  String? result;

  @override
  Widget build(BuildContext context) {
    return ASpacingColumn(
      children: [
        AForm(
          formController,
          child: AGrid.rows(
            rows: [
              AGridRow(
                areas: '6, 6',
                children: [
                  const ATextField.string("nome", labelText: "First Name", req: true),
                  const ATextField.string("sobrenome", labelText: "Last Name", req: true),
                ],
              ),
              AGridRow(
                areas: '4-4-4, 8-8-8',
                children: [
                  const ATextField.string("estado", labelText: "State", maxLength: 2, req: true),
                  const ATextField.string("cidade", labelText: "City", req: true),
                ],
              ),
              AGridRow(
                areas: '12',
                children: [
                  const ABoolField("accept", labelText: "Agree to terms and conditions", renderType: ABoolRenderType.checkbox),
                ],
              ),
              AGridRow(
                areas: '12',
                children: [
                  const AFormValidationPanel(),
                ],
              ),
              AGridRow(
                areas: '12',
                children: [
                  ASpacingRow(
                    children: [
                      ElevatedButton(
                        style: primaryButtonStyle,
                        onPressed: _enviar,
                        child: const Text("Submit"),
                      ),
                      ElevatedButton(
                        onPressed: _preencher,
                        child: const Text("Show Values"),
                      ),
                      ElevatedButton(
                        style: warningButtonStyle,
                        onPressed: _clear,
                        child: const Text("Clear"),
                      ),
                    ],
                  ),
                ],
              )
            ],
          ),
        ),
        if (result != null) const ADivider.lineOnly(),
        if (result != null) AText(result!),
      ],
    );
  }

  void _enviar() {
    setState(() {
      result = null;
    });
    if (formController.validate()) {
      result = formController.buidlJson().toString();
    }
  }

  void _preencher() {
    formController.value = {
      "nome": "João",
      "sobrenome": "Silva",
      "estado": "SP",
      "cidade": "São Paulo",
      "accept": true,
    };
  }

  void _clear() {
    formController.clear();
  }
}
