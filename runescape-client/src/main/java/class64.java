import net.runelite.mapping.ObfuscatedName;

@ObfuscatedName("bl")
public class class64 {
   @ObfuscatedName("f")
   int field1097 = class54.method953(24);
   @ObfuscatedName("k")
   int field1098 = class54.method953(24) + 1;
   @ObfuscatedName("n")
   int field1099 = class54.method953(8);
   @ObfuscatedName("i")
   int field1100 = class54.method953(16);
   @ObfuscatedName("g")
   int field1101 = class54.method953(6) + 1;
   @ObfuscatedName("e")
   int field1102 = class54.method953(24);
   @ObfuscatedName("a")
   int[] field1103;

   class64() {
      int[] var1 = new int[this.field1101];

      int var2;
      for(var2 = 0; var2 < this.field1101; ++var2) {
         int var3 = 0;
         int var4 = class54.method953(3);
         boolean var5 = class54.method978() != 0;
         if(var5) {
            var3 = class54.method953(5);
         }

         var1[var2] = var3 << 3 | var4;
      }

      this.field1103 = new int[this.field1101 * 8];

      for(var2 = 0; var2 < this.field1101 * 8; ++var2) {
         this.field1103[var2] = (var1[var2 >> 3] & 1 << (var2 & 7)) != 0?class54.method953(8):-1;
      }

   }

   @ObfuscatedName("i")
   void method1142(float[] var1, int var2, boolean var3) {
      int var4;
      for(var4 = 0; var4 < var2; ++var4) {
         var1[var4] = 0.0F;
      }

      if(!var3) {
         var4 = class54.field999[this.field1099].field978;
         int var5 = this.field1097 - this.field1102;
         int var6 = var5 / this.field1098;
         int[] var7 = new int[var6];

         for(int var8 = 0; var8 < 8; ++var8) {
            int var9 = 0;

            while(var9 < var6) {
               int var10;
               int var11;
               if(var8 == 0) {
                  var10 = class54.field999[this.field1099].method926();

                  for(var11 = var4 - 1; var11 >= 0; --var11) {
                     if(var9 + var11 < var6) {
                        var7[var9 + var11] = var10 % this.field1101;
                     }

                     var10 /= this.field1101;
                  }
               }

               for(var10 = 0; var10 < var4; ++var10) {
                  var11 = var7[var9];
                  int var12 = this.field1103[var11 * 8 + var8];
                  if(var12 >= 0) {
                     int var13 = this.field1102 + var9 * this.field1098;
                     class52 var14 = class54.field999[var12];
                     int var15;
                     if(this.field1100 == 0) {
                        var15 = this.field1098 / var14.field978;

                        for(int var16 = 0; var16 < var15; ++var16) {
                           float[] var20 = var14.method933();

                           for(int var18 = 0; var18 < var14.field978; ++var18) {
                              var1[var13 + var16 + var18 * var15] += var20[var18];
                           }
                        }
                     } else {
                        var15 = 0;

                        while(var15 < this.field1098) {
                           float[] var19 = var14.method933();

                           for(int var17 = 0; var17 < var14.field978; ++var17) {
                              var1[var13 + var15] += var19[var17];
                              ++var15;
                           }
                        }
                     }
                  }

                  ++var9;
                  if(var9 >= var6) {
                     break;
                  }
               }
            }
         }

      }
   }
}
