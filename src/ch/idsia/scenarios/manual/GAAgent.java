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

	// ��`�q���
	public byte[] gene;

	// �e�̂̕]���l�ۑ��p�ϐ�
	public float fitness;

	// ������擾�������(input)��
	public int inputNum = 16;

	// �����p�ϐ�r
	Random r = new Random();

	public GAAgent() {
		super(name);

		// 16�r�b�g�̓��͂Ȃ̂ŁA65536�i=2^16�j��
		gene = new byte[(1 << inputNum)];

		// �o��(output)��32(=2^5)�p�^�[��
		int num = 1 << (Environment.numberOfKeys - 1);

		// gene�̏����l�͗����Ŏ擾
		for (int i = 0; i < gene.length; i++) {
			gene[i] = (byte) r.nextInt(num);
		}

		// �]���l��0�ŏ�����
		fitness = 0;
	}

	// �V�����C���X�^���X���擾���郁�\�b�h
	public GAAgent getNewInstance() {
		return new GAAgent();
	}

	// ��`�q�����R�s�[���郁�\�b�h
	public GAAgent copy() {
		GAAgent h = new GAAgent();
		for (int i = 0; i < gene.length; i++) {
			h.gene[i] = this.gene[i];
		}
		return h;
	}

	// action�ioutput�j�����肷��
	public boolean[] getAction() {
		int input = 0;

		// ����񂩂�input������
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

		// input����output�iact�j�����肷��
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

	// �ߖT�}�X��������擾���郁�\�b�h
	private int probe(int i, int j, boolean checkscene) {

		// ���S����̑��Έʒu���w�肷��
		int x = i + marioEgoRow;
		int y = j + marioEgoCol;

		// checkscene��true�Ȃ�enemies���𔻒肷��
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

		// checkscene��false�Ȃ�levelScene���𔻒肷��
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
