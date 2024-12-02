package info.agilite.boot.modules.rot.domain

enum class MenuItemType {
  header,
  group,
  item,
  divider
}
data class FrontEndMenuItem(
  val id: Long,
  val title: String,
  val route: String,
  val type: MenuItemType,
  val iconCode: Int?,
  val children: List<FrontEndMenuItem>?
) {
  companion object {
    fun header(id: Long, title: String, children: List<FrontEndMenuItem>): FrontEndMenuItem {
      return FrontEndMenuItem(id, title, "", MenuItemType.header, null, children)
    }
    fun group(id: Long, title: String, iconCode: Int? = null, children: List<FrontEndMenuItem>): FrontEndMenuItem {
      return FrontEndMenuItem(id, title, "", MenuItemType.group, iconCode, children)
    }
    fun item(id: Long, title: String, route: String, iconCode: Int? = null): FrontEndMenuItem {
      return FrontEndMenuItem(id, title, route, MenuItemType.item, iconCode, null)
    }
    fun divider(id: Long): FrontEndMenuItem {
      return FrontEndMenuItem(id, "", "", MenuItemType.divider, null, null)
    }
  }
}
