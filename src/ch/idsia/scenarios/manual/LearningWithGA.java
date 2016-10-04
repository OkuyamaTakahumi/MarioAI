/* MarioAI Manual by Tanaka.N, Hasegawa.K, Emoto.R, Sugihara.Y, Ngonphachanh.A
 * from The University of Electro-Communications, Japan, in 2012, 25, Oct. */

package ch.idsia.scenarios.manual;

import java.util.Random;
import ch.idsia.benchmark.tasks.LearningTask;
import ch.idsia.agents.*;
import ch.idsia.benchmark.mario.environments.Environment;
import ch.idsia.benchmark.mario.engine.GlobalOptions;
import ch.idsia.utils.wox.serial.Easy;

public class LearningWithGA implements LearningAgent {

	// 1���゠����̌̐�100�i���㐔��100�Ƃ����̂Łj
	final int popsize = 1000;

	// ���������Ȃ��̐�2
	final int bestnum = 2;

	// �ˑR�ψٗ�10%,������50%
	final float mutateRate = 0.1f;
	final float crossRate = 0.5f;

	// Task,Agent�Ȃǂ̕ϐ�
	private LearningTask task = null;
	private String name = "LearningWithGA";
	private GAAgent[] agents;
	private Agent bestAgent;

	// �]���ő�l�ێ��p�ϐ�
	float fmax;

	// �����p�ϐ�r
	Random r = new Random();

	// LearningWithGA�R���X�g���N�^
	public LearningWithGA() {

		// �]���l��������
		fmax = 0;

		// �̐��������i����̏ꍇ100�́jAgent���쐬
		agents = new GAAgent[popsize];
		for (int i = 0; i < agents.length; i++) {
			agents[i] = new GAAgent();
		}

		// agents[0]��bestAgent�Ƃ��ď�����
		bestAgent = agents[0];
	}

	public void learn() {
		// 100�̂̕]��
		compFit();
		GAAgent nextagents[] = new GAAgent[popsize];

		// �G���[�g�헪�ɂ����2�̎c��
		for (int i = 0; i < bestnum; i++) {
			nextagents[i] = agents[i].copy();
		}

		// ���[���b�g�헪�ɂ���đI���C����
		for (int i = bestnum; i < popsize; i += 2) {
			int[] parentsGene = select();
			cross(nextagents, parentsGene, i);
		}

		// Agent���R�s�[
		agents = nextagents;

		// �ˑR�ψ�
		mutate();
	}

	// XML�t�@�C�������o��
	private void writeFile() {
		String fileName = "output/GA/" + name + "-" 
				+ GlobalOptions.getTimeStamp()	+ ".xml";
		Easy.save(bestAgent, fileName);
	}

	// �̂̕]��
	private void compFit() {
		float f = 0.0f;

		// 100�̂̕]���l���Z�o
		for (int i = 0; i < agents.length; i++) {
			agents[i].fitness = task.evaluate(agents[i]);
			if (f < agents[i].fitness) {
				f = agents[i].fitness;
				bestAgent = agents[i];
			}
		}

		// fitness���������i�~���j��Agent���\�[�g
		for (int i = 0; i < agents.length - 1; i++) {
			for (int j = i + 1; j < agents.length; j++) {
				if (agents[j].fitness > agents[i].fitness) {
					GAAgent temp = agents[i];
					agents[i] = agents[j];
					agents[j] = temp;
				}
			}
		}

		// �t�@�C���ɏ����o���i�ō����_�𒴂����ꍇ�̂݁j
		if (fmax < f) {
			fmax = f;
			System.out.println(" fmax = " + fmax);
			writeFile();
		}
	}

	// �����̐e�ƂȂ�2�̈�`�q�����[���b�g�헪�ɂ���đI��
	private int[] select() {

		// totalFitness�ێ��p�ϐ�
		float total = 0;
		for (int i = 0; i < popsize; i++) {
			total += agents[i].fitness;
		}

		// �e�ƂȂ�̔ԍ���ێ�����ϐ�
		int[] parentsGene = { 0, 0 };

		// ����rr,���v�_tf
		float rr, tf;

		/* ���[���b�g�헪 */
		// �e��1
		rr = r.nextFloat() * total;
		tf = 0;
		for (int i = 0; i < popsize; i++) {
			tf += agents[i].fitness;
			if (rr < tf) {
				parentsGene[0] = i;
				break;
			}
		}

		// �e��2
		do {
			rr = r.nextFloat() * total;
			tf = 0;
			for (int i = 0; i < popsize; i++) {
				tf += agents[i].fitness;
				if (rr < tf) {
					parentsGene[1] = i;
					break;
				}
			}
		} while (parentsGene[0] != parentsGene[1]);

		return parentsGene;
	}

	// ����
	private void cross(GAAgent[] nextagents, int[] parentsGene, int num) {

		// ������̌̕ۑ��ϐ�
		nextagents[num] = new GAAgent();
		nextagents[num + 1] = new GAAgent();

		// crossRate�̊����Ō������s�Ȃ�
		for (int j = 0; j < nextagents[0].gene.length; j++) {
			if (r.nextFloat() < crossRate) {
				nextagents[num].gene[j] = agents[parentsGene[0]].gene[j];
				nextagents[num + 1].gene[j] = agents[parentsGene[1]].gene[j];
			} else {
				nextagents[num].gene[j] = agents[parentsGene[1]].gene[j];
				nextagents[num + 1].gene[j] = agents[parentsGene[0]].gene[j];
			}
		}

	}

	// �ˑR�ψ�
	private void mutate() {

		// bestAgent�������ēˑR�ψ�
		for (int i = 1; i < popsize; i++) {
			for (int j = 0; j < agents[0].gene.length; j++) {

				// mutateRate�̊����œˑR�ψق��N����
				if (r.nextFloat() < mutateRate) {

					// �ˑR�ψٌ�̈�`�q�ۑ��p�ϐ�
					byte mutatedGene;
					do {
						mutatedGene = (byte) (r.nextFloat() * 
								(1 << Environment.numberOfKeys-1));
					} while (agents[i].gene[j] == mutatedGene);

					// �ˑR�ψٌ�̈�`�q���擾
					agents[i].gene[j] = mutatedGene;
				}
			}
		}
	}

	public void giveReward(float r) {}
	public void newEpisode() {}
	public void setEvaluationQuota(long num) {}
	public void init() {}
	public void giveIntermediateReward(float intermediateReward) {}

	public Agent getBestAgent() {return bestAgent;}
	public void setLearningTask(LearningTask ltask) {task = ltask;}
	public void setName(String n) {name = n;}
	public String getName() {return name;}
	public void reset() {bestAgent.reset();}

	public void setObservationDetails(final int rfWidth, final int rfHeight,
			final int egoRow, final int egoCol) {
		bestAgent.setObservationDetails(rfWidth, rfHeight, egoRow, egoCol);
	}

	public void integrateObservation(Environment env) {
		bestAgent.integrateObservation(env);
	}

	public boolean[] getAction() {
		return bestAgent.getAction();
	}
}