package local.test.framework
//
//import org.testng.IMethodInstance
//import org.testng.ITestContext
//
//class Interceptor : IMethodInterceptor {
//    override fun intercept(methods: List<IMethodInstance?>?, context: ITestContext?): List<IMethodInstance?>? {
//        val result: MutableList<IMethodInstance> = ArrayList()
//        for (m in methods!!) {
//            if (m!!.method.isTest){
//                var groupList = mutableListOf<String>()
//                val groups = m!!.method.groups
//                for (group in groups) {
//                    groupList.add(group)
//                }
//                if (groupList.contains("gr1")) {
//                    result.add(0, m)
//                } else {
//                    result.add(m)
//                }
//            }
//
//        }
//        return result
//    }
//
//}