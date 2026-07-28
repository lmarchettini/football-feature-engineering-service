package com.footballai.engineering.service.service;

import java.math.BigDecimal;

public record TeamStats(

        BigDecimal form,

        BigDecimal avgGoalsScored,

        BigDecimal avgGoalsConceded,

        /**
         * Media dei gol totali
         * (gol segnati + gol subiti).
         */
        BigDecimal avgTotalGoals,

        BigDecimal winRate,

        BigDecimal cleanSheetRate,

        /**
         * Percentuale di partite
         * in cui la squadra ha segnato
         * almeno un gol.
         */
        BigDecimal scoredRate,

        /**
         * Percentuale di partite
         * in cui la squadra ha subito
         * almeno un gol.
         */
        BigDecimal concededRate,

        Integer restDays,

        BigDecimal avgShots,

        BigDecimal avgShotsOnGoal,

        BigDecimal avgPossession,

        BigDecimal avgPassAccuracy,

        BigDecimal avgCorners,

        BigDecimal avgShotsInsideBox,

        BigDecimal avgXg,

        BigDecimal avgXga,

        /*
         * Percentuale di partite in cui entrambe
         * le squadre hanno segnato.
         */
        BigDecimal bttsRate,

        /*
         * Percentuale di partite terminate
         * con almeno 3 gol complessivi.
         */
        BigDecimal over25Rate

) {
}