package net.runelite.deob.deobfuscators;

import java.util.ArrayList;

import net.runelite.deob.ClassFile;
import net.runelite.deob.ClassGroup;
import net.runelite.deob.Deobfuscator;
import net.runelite.deob.Field;
import net.runelite.deob.Method;
import net.runelite.deob.attributes.Code;
import net.runelite.deob.attributes.code.Instruction;
import net.runelite.deob.attributes.code.instruction.types.FieldInstruction;
import net.runelite.deob.attributes.code.instruction.types.GetFieldInstruction;
import net.runelite.deob.attributes.code.instruction.types.SetFieldInstruction;

public class UnusedFields implements Deobfuscator
{
	private static boolean isUnused(ClassGroup group, Field field)
	{
		int get = 0, set = 0;
		for (ClassFile cf : group.getClasses())
			for (Method m : cf.getMethods().getMethods())
			{
				Code code = m.getCode();
				if (code == null)
					continue;
				
				for (Instruction ins : code.getInstructions().getInstructions())
				{
					if (ins instanceof FieldInstruction)
					{
						FieldInstruction fi = (FieldInstruction) ins;
						
						if (fi.getMyField() != field)
							continue;

						if (ins instanceof GetFieldInstruction)
							++get;
						if (ins instanceof SetFieldInstruction)
							++set;
					}
				}
			}
		
		if (get == 0)
			return true;
		
		return false;
	}
	
	@Override
	public void run(ClassGroup group)
	{
		int count = 0;
		for (ClassFile cf : group.getClasses())
			for (Field f : new ArrayList<>(cf.getFields().getFields()))
			{
				if (isUnused(group, f))
				{
					cf.getFields().getFields().remove(f);
					++count;
				}
			}
		System.out.println("Removed " + count + " unused fields");
	}
}
