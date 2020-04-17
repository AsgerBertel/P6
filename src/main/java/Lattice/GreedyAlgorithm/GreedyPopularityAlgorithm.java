package Lattice.GreedyAlgorithm;

import Lattice.Node;

import java.util.Set;

public class GreedyPopularityAlgorithm extends GreedyAlgorithm {
    public GreedyPopularityAlgorithm(Set<Node> nodes, Node rootNode) {
        super(nodes, rootNode);
    }

    @Override
    public void updateCurrentBenefit() {
        updateBenefitScale();
        super.updateCurrentBenefit();
    }

    private void updateBenefitScale() {
        /**
         * Lav en sorteret liste af nodes baseret på popularitet - Lavest --> Højest
         * Assign scaling til hver node. Baseret på hvor stor forskel der er på current node og next nodes popularitet
         *
         */
    }


}
