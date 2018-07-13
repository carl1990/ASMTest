package asm;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by Carl on 2018/6/12.
 */

public class ASMUtil {


    /**
     * 类是否满足匹配条件，满足的才会允许修改其中的方法
     * 1、实现android/view/View$OnClickListener接口的类
     * 2、类名包含Fragment就当做是继承了Fragment的类
     * 3、在app的module中手动设置的监听条件：指定类名或实现接口
     *
     * @param className  类名
     * @param interfaces 类的实现接口
     */
    public static boolean isMatchingClass(String className, String[] interfaces) {
        boolean isMeetClassCondition = false;
        //剔除掉以android开头的类，即系统类，以避免出现不可预测的bug
        if (className.startsWith("android")) {
            return isMeetClassCondition;
        }
        // 是否满足实现的接口
        isMeetClassCondition = isMatchingInterfaces(interfaces, "android/view/View$OnClickListener");
        if (className.contains("Fragment")) {
            isMeetClassCondition = true;
        }
        return isMeetClassCondition;
    }


    /**
     * 接口名是否匹配
     *
     * @param interfaces    类的实现接口
     * @param interfaceName 需要匹配的接口名
     */
    private static boolean isMatchingInterfaces(String[] interfaces, String interfaceName) {
        boolean isMatch = false;
        // 是否满足实现的接口
        for (String anInterface : interfaces) {
            if (anInterface.equals(interfaceName)) {
                isMatch = true;
            }
        }
        return isMatch;
    }


    /**
     * 方法是否匹配到，根据方法名和参数的描述符来确定一个方法是否需要修改的初步条件，
     * 1、View的onClick(View v)方法
     * 2、Fragment的onResume()方法
     * 3、Fragment的onPause()方法
     * 4、Fragment的setUserVisibleHint(boolean b)方法
     * 5、Fragment的onHiddenChanged(boolean b)方法
     * 6、在app的module中手动设置的监听条件：指定方法或注解方法
     * 可以扩展自己想监听的方法
     *
     * @param name 方法名
     * @param desc 参数的方法的描述符
     */
    static boolean isMatchingMethod(String name, String desc) {
        if ((name.equals("onClick") && desc.equals("(Landroid/view/View;)V"))
            || (name.equals("onResume") && desc.equals("()V"))
            || (name.equals("onPause") && desc.equals("()V"))
            || (name.equals("setUserVisibleHint") && desc.equals("(Z)V"))
            || (name.equals("onHiddenChanged") && desc.equals("(Z)V"))
            ) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取自动埋点的方法修改器
     *
     * @param interfaces    实现的接口名
     * @param className     类名
     * @param methodVisitor 需要修改的方法
     * @param name          方法名
     * @param desc          参数描述符
     */
    static MethodVisitor getMethodVisitor(String[] interfaces, String className, String superName,
                                          MethodVisitor methodVisitor, int access, String name, String desc) {
        MethodVisitor adapter = null;

        if (name == "onClick" && isMatchingInterfaces(interfaces, "android/view/View$OnClickListener")) {
            adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodEnter() {
                    super.onMethodEnter();

                    // ALOAD 25 获取方法的第一个参数
                    methodVisitor.visitVarInsn(ALOAD, 1);
                    // INVOKESTATIC INVOKESTATIC
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onClick", "(Landroid/view/View;)V", false);
                }
            };
        } else if (name == "onResume") {
            if (className.contains("Fragment") && superName.contains("Fragment")) {
                adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // ALOAD
                        methodVisitor.visitVarInsn(ALOAD, 0);
                        // INVOKESTATIC INVOKESTATIC
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onFragmentResume", "(Landroid/support/v4/app/Fragment;)V", false);
                    }
                };
            } else if (className.contains("Activity") && superName.contains("Activity")) {
                adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // ALOAD
                        methodVisitor.visitVarInsn(ALOAD, 0);
                        // INVOKESTATIC INVOKESTATIC
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onActivityResume", "(Landroid/app/Activity;)V", false);
                    }
                };
            }

        } else if (name == "onPause" && className.contains("Fragment")) {
            if (className.contains("Fragment") && superName.contains("Fragment")) {
                adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // ALOAD 25
                        methodVisitor.visitVarInsn(ALOAD, 0);
                        // INVOKESTATIC INVOKESTATIC
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onFragmentPause", "(Landroid/support/v4/app/Fragment;)V", false);
                    }
                };
            } else if (className.contains("Activity") && superName.contains("Activity")) {
                adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                    @Override
                    protected void onMethodExit(int opcode) {
                        super.onMethodExit(opcode);
                        // ALOAD
                        methodVisitor.visitVarInsn(ALOAD, 0);
                        // INVOKESTATIC INVOKESTATIC
                        methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onActivityPause", "(Landroid/app/Activity;)V", false);
                    }
                };
            }
        } else if (name == "setUserVisibleHint" && className.contains("Fragment")) {
            adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode);
                    // ALOAD 25
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    // ILOAD 21
                    methodVisitor.visitVarInsn(ILOAD, 1);
                    // INVOKESTATIC INVOKESTATIC
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "setFragmentUserVisibleHint", "(Landroid/support/v4/app/Fragment;Z)V", false);
                }
            };
        } else if (name == "onHiddenChanged" && className.contains("Fragment")) {
            adapter = new CarlMethodVisitor(methodVisitor, access, name, desc) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode);
                    // ALOAD 25
                    methodVisitor.visitVarInsn(ALOAD, 0);
                    // ILOAD 21
                    methodVisitor.visitVarInsn(ILOAD, 1);
                    // INVOKESTATIC INVOKESTATIC
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "com/carl/asmtest/tack/TrackHelper", "onFragmentHiddenChanged", "(Landroid/support/v4/app/Fragment;Z)V", false);
                }
            };
        }
        return adapter;
    }

}
