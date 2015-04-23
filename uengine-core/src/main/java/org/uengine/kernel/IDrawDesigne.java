package org.uengine.kernel;

public interface IDrawDesigne {
	
	/**
	 * 디자인 타임에서 사용
	 * 디자인이 그려질때, 처음 실행하는 메서드
	 * 객체 생성, init data 셋팅에 쓰임
	 * @throws Exception
	 */
	public void drawInit() throws Exception;
	
}
