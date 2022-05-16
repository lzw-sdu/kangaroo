package com.faceunity.app_ptag.compat

import com.faceunity.fupta.avatar_data.entity.resource.*
import com.faceunity.fupta.avatar_data.parser.IResourceParser
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

/**
 * 一个资源解析器的实现类。
 * 采用了 GSON 作为解析，开发者可自行用对应的解析方案。
 */
class GsonResourceParserImpl : IResourceParser {
    private val gson = Gson()

    override fun parseAvatar(json: String): AvatarResource {
        return gson.fromJson(json, AvatarResource::class.java)
    }

    override fun parseItemList(json: String): ItemListResource {
        return gson.fromJson(json, ItemListResource::class.java)
    }

    override fun parseProject(json: String): ProjectResource {
        return gson.fromJson(json, ProjectResource::class.java)
    }

    override fun parseEditConfig(json: String): EditConfigResource {
        val jsonObj = JsonParser.parseString(json).asJsonObject

        val sourceJsonObj = jsonObj.getAsJsonObject("source")
        val sourceMap = gson.fromJson<Map<String, EditConfigResource.Source.SourceItem>>(
            sourceJsonObj,
            object : TypeToken<Map<String, EditConfigResource.Source.SourceItem>>() {}.type
        )
        val source = EditConfigResource.Source(sourceMap)

        val colorKeysJsonObj = jsonObj.getAsJsonObject("color_keys")
        val colorKeysMap = gson.fromJson<Map<String, List<String>>>(
            colorKeysJsonObj,
            object : TypeToken<Map<String, List<String>>>() {}.type
        )
        val colorKeys = EditConfigResource.ColorKeys(colorKeysMap)

        val map = mutableMapOf<String, List<String>>()
        val keySet = jsonObj.keySet().minus(setOf("source", "color_keys"))
        keySet.forEach { key ->
            val jsonArray = jsonObj.getAsJsonArray(key)
            val strings = gson.fromJson(jsonArray, Array<String>::class.java)
            map[key] = strings.toList()
        }

        return EditConfigResource(map, colorKeys, source)
    }

    override fun parseEditCustomConfig(json: String): EditCustomConfigResource {
        val jsonObj = JsonParser.parseString(json).asJsonObject

        val sourceJsonObj = jsonObj.getAsJsonObject("source")
        val sourceMap = mutableMapOf<String, EditCustomConfigResource.Source.SourceItem>()
        sourceJsonObj.keySet().forEach { key ->
            val itemJsonObj = sourceJsonObj.getAsJsonObject(key)
            val itemMap = gson.fromJson<Map<String, Any?>>(
                itemJsonObj,
                object : TypeToken<Map<String, Any?>>() {}.type
            )
//            itemJsonObj.keySet().forEach { itemKey ->
//                itemJsonObj.get(itemKey).
//            }
            sourceMap[key] = EditCustomConfigResource.Source.SourceItem(itemMap)
        }

        val source = EditCustomConfigResource.Source(sourceMap)

        val colorKeysJsonObj = jsonObj.getAsJsonObject("color_keys")
        val colorKeysMap = gson.fromJson<Map<String, List<String>>>(
            colorKeysJsonObj,
            object : TypeToken<Map<String, List<String>>>() {}.type
        )
        val colorKeys = EditCustomConfigResource.ColorKeys(colorKeysMap)

        val map = mutableMapOf<String, List<String>>()
        val keySet = jsonObj.keySet().minus(setOf("source", "color_keys"))
        keySet.forEach { key ->
            val jsonArray = jsonObj.getAsJsonArray(key)
            val strings = gson.fromJson(jsonArray, Array<String>::class.java)
            map[key] = strings.toList()
        }

        return EditCustomConfigResource(map, colorKeys, source)
    }

    override fun parseEditItemList(json: String): EditItemListResource {
        val jsonObj = JsonParser.parseString(json).asJsonObject

        val mapObj = jsonObj.getAsJsonObject("map")
        val map = gson.fromJson<Map<String, List<EditItemListResource.Item>>>(
            mapObj,
            object : TypeToken<Map<String, List<EditItemListResource.Item>>>() {}.type
        )
        val version = jsonObj.get("version").let {
            if (it == null || it.isJsonNull) {
                -1
            } else {
                it.asInt
            }
        }
        return EditItemListResource(map, version)
    }

//    override fun parserEditItemList(json: String): EditItemListCustomResource {
//        val jsonObj = JsonParser.parseString(json).asJsonObject
//
//        val mapObj = jsonObj.getAsJsonObject("map")
//        val map = mutableMapOf<String, List<EditItemListCustomResource.Item>>()
//        mapObj.keySet().forEach { key ->
//            val itemArray = mapObj.getAsJsonArray(key)
//            val itemList = mutableListOf<EditItemListCustomResource.Item>()
//            itemArray.forEach { item ->
//                val itemMap = mutableMapOf<String, Any?>()
//                item.asJsonObject.keySet().forEach {
//                    itemMap[it] = item.asJsonObject
//                }
//                val customItem = EditItemListCustomResource.Item(itemMap)
//                itemList.add(customItem)
//            }
//
//            map[key] = itemList
//        }
//        val version = jsonObj.get("version").let {
//            if (it == null || it.isJsonNull) {
//                -1
//            } else {
//                it.asInt
//            }
//        }
//        return EditItemListCustomResource(map, version)
//    }

    override fun parseEditCustomItemList(json: String): EditCustomItemListResource {
        val jsonObj = JSONObject(json)

        val mapObj = jsonObj.getJSONObject("map")
        val map = mutableMapOf<String, List<EditCustomItemListResource.Item>>()
        mapObj.keys().forEach { key ->
            val itemArray = mapObj.optJSONArray(key)
            val itemList = mutableListOf<EditCustomItemListResource.Item>()
            for (i in 0 until itemArray.length()) {
                val item = itemArray.getJSONObject(i)

                val itemMap = mutableMapOf<String, Any?>()
                item.keys().forEach {
                    itemMap[it] = item.get(it)
                }
                val customItem = EditCustomItemListResource.Item(itemMap)
                itemList.add(customItem)
            }

            map[key] = itemList
        }
        val version = jsonObj.optInt("version")
        return EditCustomItemListResource(map, version)
    }

    override fun parseEditColorList(json: String): EditColorListResource {
        val jsonObj = JsonParser.parseString(json).asJsonObject

        val map = gson.fromJson<Map<String, List<EditColorListResource.Color>>>(
            jsonObj,
            object : TypeToken<Map<String, List<EditColorListResource.Color>>>() {}.type
        )
        return EditColorListResource(map)
    }

    override fun parseSceneList(json: String): SceneListResource {
        return gson.fromJson(json, SceneListResource::class.java)
    }

    override fun serializeAvatar(avatarResource: AvatarResource): String {
        return gson.toJson(avatarResource)
    }

//    fun editItemListToJson(editItemListResource: EditItemListResource): String {
//        return gson.toJson(editItemListResource)
//    }
//
//    fun buildEditItemList(editConfigResource: EditConfigResource, itemListResource: ItemListResource) : EditItemListResource {
////        val filterList = itemListResource.list.filter { it.tags.project.any { filterTag.contains(it) } }
//
//        val map = mutableMapOf<String, List<EditItemListResource.Item>>()
//        editConfigResource.map.values.forEach {
//            it.forEach { menuKey ->
//                val filter = itemListResource.list.filter { it.tags.project.contains(menuKey) }
//                if (filter.size > 0) {
//                    map[menuKey] = filter.map {
//                        val filter = mutableMapOf<String, String>()
//                        it.tags.project.forEach {
//                            when(it) {
//                                "male" -> filter["gender"] = "male"
//                                "female" -> filter["gender"] = "female"
//                            }
//                        }
//                        EditItemListResource.Item(it.path, filter)
//                    }
//                }
//            }
//        }
//
//        return EditItemListResource(map, itemListResource.version)
//    }
}