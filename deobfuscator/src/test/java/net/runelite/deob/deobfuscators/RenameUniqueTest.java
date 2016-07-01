/*
 * Copyright (c) 2016, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by Adam <Adam@sigterm.info>
 * 4. Neither the name of the Adam <Adam@sigterm.info> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY Adam <Adam@sigterm.info> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL Adam <Adam@sigterm.info> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.deob.deobfuscators;

import java.io.File;
import java.io.IOException;
import net.runelite.asm.ClassFile;
import net.runelite.asm.ClassGroup;
import net.runelite.asm.Field;
import net.runelite.asm.Method;
import net.runelite.asm.attributes.Code;
import net.runelite.asm.attributes.code.Instruction;
import net.runelite.asm.attributes.code.instruction.types.InvokeInstruction;
import net.runelite.deob.Deob;
import net.runelite.deob.TemporyFolderLocation;
import net.runelite.deob.util.JarUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class RenameUniqueTest
{
	private static final File GAMEPACK = new File(RenameUniqueTest.class.getResource("/gamepack_v16.jar").getFile());
	
	@Rule
	public TemporaryFolder folder = TemporyFolderLocation.getTemporaryFolder();
	
	private ClassGroup group;
	
	@Before
	public void before() throws IOException
	{
		group = JarUtil.loadJar(GAMEPACK);
	}
	
	@After
	public void after() throws IOException
	{
		JarUtil.saveJar(group, folder.newFile());
	}
	
	@Test
	public void testRun() throws IOException
	{
		RenameUnique renameUnique = new RenameUnique();
		renameUnique.run(group);

		checkRenamed();
	}
	
	private void checkRenamed()
	{
		for (ClassFile cf : group.getClasses())
		{
			Assert.assertTrue(cf.getName().startsWith("class") || cf.getName().equals("client"));

			for (Field f : cf.getFields().getFields())
			{
				// synthetic fields arent obfuscated
				if (f.isSynthetic())
					continue;
				
				Assert.assertTrue(f.getName().startsWith("field"));
			}

			for (Method m : cf.getMethods().getMethods())
			{
				Assert.assertTrue(m.getName().length() > 2);

				checkCode(m.getCode());
			}
		}
	}

	private void checkCode(Code code)
	{
		if (code == null)
			return;

		for (Instruction i : code.getInstructions().getInstructions())
		{
			if (!(i instanceof InvokeInstruction))
				continue;

			InvokeInstruction ii = (InvokeInstruction) i;
			Assert.assertTrue(ii.getMethod().getName().length() > Deob.OBFUSCATED_NAME_MAX_LEN
				|| ii.getMethod().getClazz().getName().length() > Deob.OBFUSCATED_NAME_MAX_LEN);
		}
	}
}
