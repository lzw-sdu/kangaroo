import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:get/get.dart';
import 'package:kangaroo/router/index.dart';
import 'package:kangaroo/style/theme.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: '袋鼠运动',
      debugShowCheckedModeBanner: false,
      initialRoute: PageRoutes.initRoute,
      getPages: PageRoutes.routes,
      theme: Themes.lightTheme,
      builder: (BuildContext context, Widget? child) => Scaffold(
        // Global GestureDetector that will dismiss the keyboard
        body: GestureDetector(
          onTap: () {
            FocusScopeNode currentFocus = FocusScope.of(context);
            if (!currentFocus.hasPrimaryFocus &&
                currentFocus.focusedChild != null) {
              FocusManager.instance.primaryFocus?.unfocus();
            }
          },
          child: FlutterEasyLoading(child: child),
        ),
      ),
    );
  }
}
