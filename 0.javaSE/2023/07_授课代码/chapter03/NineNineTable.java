/*
练习：九九乘法表

*/
class NineNineTable {
	public static void main(String[] args) {
		
		for(int i = 1;i <= 9;i++){
			
			for(int j = 1;j <= i;j++){
				
				System.out.print(i + "*" + j + "=" + i * j + "\t");
			
			}

			System.out.println();
		
		}

	}
}
