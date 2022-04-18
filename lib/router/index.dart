import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kangaroo/common/request/http_request.dart';
import 'package:kangaroo/pages/child/child_home_binding.dart';
import 'package:kangaroo/pages/child/child_home_view.dart';
import 'package:kangaroo/pages/login/login_page/login_view.dart';
import 'package:kangaroo/pages/parent/parent_main_page/parent_main_binding.dart';
import 'package:kangaroo/pages/parent/parent_main_page/parent_main_view.dart';
import 'package:kangaroo/router/transition.dart';

class PageName {
  static const String login = '/login';
  static const String childHome = '/childHome';
  static const String parentHome = '/parentHome';
}

class PageRoutes {
  static final String initRoute = PageName.login;
  static final List<GetPage<dynamic>> routes = [
    GetPage(
      name: PageName.login,
      page: () => LoginPage(),
      customTransition: MyRouterEnterDirBottomToTop(),
      transitionDuration: const Duration(milliseconds: 370),
    ),
    GetPage(
      name: PageName.childHome,
      page: () => ChildHomePage(),
      binding: ChildHomeBinding(),
      customTransition: MyRouterEnterDirBottomToTop(),
      transitionDuration: const Duration(milliseconds: 370),
    ),
    GetPage(
      name: PageName.parentHome,
      page: () => ParentMainPage(),
      binding: ParentMainBinding(),
      customTransition: MyRouterEnterDirRightToLft(),
      transitionDuration: const Duration(milliseconds: 370),
    ),
    /*


    GetPage(
      name: PageName.videoDetaill,
      page: () => const VideoDetaill(),
      transition: Transition.fadeIn,
    ),
    GetPage(
      name: PageName.typeDetaill,
      page: () => const TypeDetaill(),
      customTransition: MyRouterEnterDirRightToLft(),
      transitionDuration: const Duration(milliseconds: 370),
    ),
    GetPage(
      name: PageName.userDeclare,
      page: () => const UserDeclare(),
      customTransition: MyRouterEnterDirRightToLft(),
      transitionDuration: const Duration(milliseconds: 370),
    ),
    GetPage(
      name: PageName.videoHistory,
      page: () => const VideoHistory(),
      binding: BindingsBuilder.put(() => VideoHistoryStore()),
      customTransition: MyRouterEnterDirRightToLft(),
      transitionDuration: const Duration(milliseconds: 370),
    ),*/
  ];

  static addRouter({
    required String routeName,
    Map<String, String>? parameters,
  }) async {
    // 先清除所有的dio cancel
    Http().cancelRequests();
    //ToDo: 判断登录状态
    // 再router入栈
    Get.toNamed(
      routeName,
      parameters: parameters,
    );
  }
}
