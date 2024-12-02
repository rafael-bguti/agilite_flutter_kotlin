package info.agilite.shared.xml

import info.agilite.shared.exceptions.ValidationException
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.Serializable
import javax.management.AttributeNotFoundException

class ElementXml : Serializable {
  val domElement: Element

  companion object {
    fun create(tag: String, namespace: String? = null): ElementXml {
      return ElementXmlConverter.createElement(tag, namespace)
    }
  }

  constructor(document: Document) {
    domElement = document.documentElement
  }

  constructor(element: Element) {
    domElement = element
  }

  /*=============================================*/
  /*=============METODOS PARA ESCRITA============*/
  fun addNode(tagName: String): ElementXml {
    val child = domElement.ownerDocument.createElement(tagName.trim())
    domElement.appendChild(child)
    return ElementXml(child)
  }

  fun addNode(elementXml: ElementXml) {
    domElement.appendChild(elementXml.domElement)
  }

  fun addNode(tagName: String, value: Any?, required: Boolean = true): ElementXml? {
    if (value == null) {
      if (required) throw ValidationException("Valor requerido para tag '$tagName' não informado")
      return null
    }
    val child = domElement.ownerDocument.createElement(tagName.trim())
    domElement.appendChild(child)
    val elementXml = ElementXml(child)
    elementXml.setNodeValue(value.toString())
    return elementXml
  }

  fun setNodeValue(value: String?): ElementXml {
    val childs = domElement.childNodes
    if (childs.length > 0) {
      for (i in 0 until childs.length) {
        domElement.removeChild(childs.item(i))
      }
    }
    domElement.appendChild(domElement.ownerDocument.createTextNode(value))
    return this
  }

  fun setAttribute(name: String?, value: String?): ElementXml {
    domElement.setAttribute(name, value)
    return this
  }

  fun removeAttribute(attributeName: String?): ElementXml {
    domElement.removeAttribute(attributeName)
    return this
  }

  /*=============================================*/ /*=============METODOS PARA LEITURA============*/
  fun getChildNode(name: String): ElementXml? {
    if (name.contains(".")) return findChildNode(name)
    val childNodes = getChildNodes(name) ?: return null
    if (childNodes.size > 1) throw RuntimeException("A busca no XML pelo elemento de nome '$name' retornou mais de uma ocorrência, utilize a busca especifica para listas")
    return childNodes[0]
  }

  fun getChildNodes(name: String): List<ElementXml>? {
    if (name.contains(".")) return findChildNodes(name)
    return getDirectChildNodesOnThisElement(name)
  }

  fun getChildValue(name: String): String? {
    if (name.contains(".")) return findChildValue(name)
    val element = getChildNode(name)
    return element?.value

  }

  fun findChildNode(query: String): ElementXml? {
    val nodes = findChildNodes(query) ?: return null
    if (nodes.size > 1) throw RuntimeException("A busca no XML pela consulta '$query' retornou mais de uma ocorrência, utilize a busca especifica para listas")
    return nodes[0]

  }

  fun findChildValue(query: String): String? {
    val child = findChildNode(query) ?: return null
    return child.value
  }

  fun findSingleChildValue(query: String): String? {
    val child = findChildValues(query) ?: emptyList()
    return child.firstOrNull()
  }


  fun findFirstChildValue(vararg queries: String): String? {
    for (query in queries) {
      val child = findChildNode(query)
      if (child != null) return child.value
    }
    return null
  }

  fun findChildValues(query: String): List<String>? {
    val childrenNode = findChildNodes(query)
    if (childrenNode != null && childrenNode.size > 0) {
      val childrenValue: MutableList<String> = ArrayList()
      for (child in childrenNode) {
        childrenValue.add(child.value)
      }
      return childrenValue
    }
    return null
  }

  fun findFirstChildNode(vararg queries: String): ElementXml? {
    for (query in queries) {
      val child = findChildNode(query)
      if (child != null) return child
    }
    return null
  }

  fun findChildNodes(query: String): MutableList<ElementXml>? {
    val names = query.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return if (names.size == 1) {
      findChildNodesOnThisElement(names[0])
    } else {
      var children = findChildNodesOnThisElement(names[0])
      for (i in 1 until names.size) {
        children = getChildNodesForAllElementsByName(children, names[i])
      }
      if (children!!.isEmpty()) null else children
    }
  }

  fun removeChildNodes(name: String) {
    val nodes = domElement.getElementsByTagName(name)
    val nodesToRemove = mutableListOf<ElementXml>()
    for (i in 0 until nodes.length) {
      nodesToRemove.add(ElementXml(nodes.item(i) as Element))
    }
    for (node in nodesToRemove) {
      domElement.removeChild(node.domElement)
    }
  }

  private fun getDirectChildNodesOnThisElement(nameParam: String): MutableList<ElementXml>? {
    var name = nameParam
    name = name.trim()
    val list = domElement.childNodes
    return createListElementsByNodeList(name, list)
  }

  private fun findChildNodesOnThisElement(name: String): MutableList<ElementXml>? {
    val list = domElement.getElementsByTagName(name)
    return createListElementsByNodeList(name, list)
  }

  private fun getChildNodesForAllElementsByName(elements: List<ElementXml>?, childName: String): MutableList<ElementXml> {
    val retorno: MutableList<ElementXml> = ArrayList()
    if (elements != null) {
      for (xml in elements) {
        val childNodes = xml.getChildNodes(childName)
        if (childNodes != null) {
          retorno.addAll(childNodes)
        }
      }
    }
    return retorno
  }

  private fun createListElementsByNodeList(name: String, list: NodeList): MutableList<ElementXml>? {
    val elements: MutableList<ElementXml> = ArrayList()
    for (i in 0 until list.length) {
      if (list.item(i).nodeType != Node.ELEMENT_NODE) continue
      if (list.item(i).nodeName == name) elements.add(ElementXml(list.item(i) as Element))
    }

    return if (elements.size == 0) null else elements
  }

  var value: String
    get() = domElement.textContent
    set(value) {
      domElement.textContent = value
    }


  fun getAttValue(name: String): String? {
    val atts = domElement.attributes
    if (atts == null || atts.length == 0) return null
    for (i in 0 until atts.length) {
      if (atts.item(i).nodeName.equals(name, ignoreCase = true)) return atts.item(i).nodeValue
    }
    throw AttributeNotFoundException("O Atributo '" + name + "' não existe no elemento '" + domElement.nodeName + "'")
  }

  fun toXmlString(): String {
    return ElementXmlConverter.element2String(this)
  }

  fun toXmlStringTrim(): String {
    return ElementXmlConverter.element2StringTrim(this)
  }

  fun cloneElementXml(): ElementXml {
    return ElementXml(domElement.cloneNode(true) as Element)
  }
}