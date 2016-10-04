/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import java.util.Random;
import ch.idsia.agents.Agent;
import ch.idsia.agents.controllers.*;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.evolution.Evolvable;
import ch.idsia.benchmark.mario.engine.GeneralizerLevelScene;
import ch.idsia.benchmark.mario.engine.sprites.*;

public class GAAgent extends BasicMarioAIAgent implements Agent, Evolvable {
	static String name = "GAAgent";

	// 遺伝子情報
	public byte[] gene;

	// 各個体の評価値保存用変数
	public float fitness;

	// 環境から取得する入力(input)数
	public int inputNum = 16;

	// 乱数用変数r
	Random r = new Random();

	public GAAgent() {
		super(name);

		// 16ビットの入力なので、65536（=2^16）個
		gene = new byte[(1 << inputNum)];

		// 出力(output)は32(=2^5)パターン
		int num = 1 << (Environment.numberOfKeys - 1);

		// geneの初期値は乱数で取得
		for (int i = 0; i < gene.length; i++) {
			gene[i] = (byte) r.nextInt(num);
		}

		// 評価値を0で初期化
		fitness = 0;
	}

	// 新しいインスタンスを取得するメソッド
	public GAAgent getNewInstance() {
		return new GAAgent();
	}

	// 遺伝子情報をコピーするメソッド
	public GAAgent copy() {
		GAAgent h = new GAAgent();
		for (int i = 0; i < gene.length; i++) {
			h.gene[i] = this.gene[i];
		}
		return h;
	}

	// action（output）を決定する
	public boolean[] getAction() {
		int input = 0;

		// 環境情報からinputを決定
		input += probe(-1, -1, true) * (1 << 15);
		input += probe(0, -1, true) * (1 << 14);
		input += probe(1, -1, true) * (1 << 13);
		input += probe(-1, 0, true) * (1 << 12);
		input += probe(1, 0, true) * (1 << 11);
		input += probe(-1, 1, true) * (1 << 10);
		input += probe(1, 1, true) * (1 << 9);

		input += probe(-1, -1, false) * (1 << 8);
		input += probe(0, -1, false) * (1 << 7);
		input += probe(1, -1, false) * (1 << 6);
		input += probe(-1, 0, false) * (1 << 5);
		input += probe(1, 0, false) * (1 << 4);
		input += probe(-1, 1, false) * (1 << 3);
		input += probe(1, 1, false) * (1 << 2);

		input += (isMarioOnGround ? 1 : 0) * (1 << 1);
		input += (isMarioAbleToJump ? 1 : 0) * (1 << 0);

		// inputからoutput（act）を決定する
		int act = gene[input];
		for (int i = 0; i < Environment.numberOfKeys; i++) {
			action[i] = (act % 2 == 1);
			act /= 2;
		}

		return action;
	}

	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
	}

	// 近傍マスから情報を取得するメソッド
	private int probe(int i, int j, boolean checkscene) {

		// 中心からの相対位置を指定する
		int x = i + marioEgoRow;
		int y = j + marioEgoCol;

		// checksceneがtrueならenemies情報を判定する
		if (checkscene) {
			switch (enemies[y][x]) {
			case Sprite.KIND_NONE:
			case Sprite.KIND_FIRE_FLOWER:
			case Sprite.KIND_MUSHROOM:
			case Sprite.KIND_FIREBALL:
				return 0;

			case Sprite.KIND_GOOMBA:
			case Sprite.KIND_SPIKY:
				return 1;

			default:
				return 1;
			}
		}

		// checksceneがfalseならlevelScene情報を判定する
		else {
			switch (levelScene[y][x]) {

			case Sprite.KIND_NONE:
			case GeneralizerLevelScene.COIN_ANIM:
				return 0;

			case GeneralizerLevelScene.BORDER_CANNOT_PASS_THROUGH:
			case GeneralizerLevelScene.FLOWER_POT_OR_CANNON:
			case GeneralizerLevelScene.BRICK:
			case GeneralizerLevelScene.BORDER_HILL:
				return 1;

			default:
				return 0;
			}
		}
	}

	public void reset() {
	}

	public void mutate() {
	}
}
