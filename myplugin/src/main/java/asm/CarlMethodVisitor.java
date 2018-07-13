package asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;


/**
 * Created by Carl on 2018/6/11.
 */

public class CarlMethodVisitor extends AdviceAdapter {
    String methodName = "";


    protected CarlMethodVisitor(MethodVisitor methodVisitor, int access, String name, String desc) {
        super(Opcodes.ASM4, methodVisitor, access, name, desc);
        methodName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        System.out.println(methodName + "visitAnnotation" + s + "===============================");
        return super.visitAnnotation(s, b);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        System.out.println(methodName + "onMethodEnter" + "===============================");
    }

    @Override
    protected void onMethodExit(int i) {
        super.onMethodExit(i);
        System.out.println(methodName + "onMethodExit" + "===============================");
    }


}
