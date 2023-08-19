package ch.lukasweibel.anschlagkasten.scheduler;

import io.quarkus.scheduler.Scheduled;

import java.time.LocalDate;
import java.util.ArrayList;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ch.lukasweibel.anschlagkasten.db.DbAccessor;
import ch.lukasweibel.anschlagkasten.model.Anschlag;

@ApplicationScoped
public class Scheduler {

    @Inject
    DbAccessor dbAccessor;

    @Scheduled(cron = "{cron.expression.deactivate.anschlaege}")
    public void deactivateOldAnschlaege() {
        ArrayList<Anschlag> activeAnschlaege = dbAccessor.getActiveAnschlaege();

        for (Anschlag anschlag : activeAnschlaege) {
            if (isInThePast(anschlag.getDate())) {
                anschlag.setStatus(2);
                dbAccessor.updateAnschlag(anschlag);
            }
        }
    }

    private boolean isInThePast(String dateString) {
        if (dateString != null) {
            LocalDate currentDate = LocalDate.now();

            int year = Integer.parseInt(dateString.substring(0, 4));
            int month = Integer.parseInt(dateString.substring(5, 7));
            int day = Integer.parseInt(dateString.substring(8, 10));

            LocalDate givenDate = LocalDate.of(year, month, day);

            if (givenDate.isBefore(currentDate)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
