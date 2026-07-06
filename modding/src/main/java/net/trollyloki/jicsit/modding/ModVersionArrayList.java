package net.trollyloki.jicsit.modding;

import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

@NullMarked
class ModVersionArrayList extends ArrayList<ModVersion> implements ModVersionList {

    @Override
    public Optional<ModVersion> findLatestSupportingBuild(int build) {
        return stream()
                .filter(version -> !version.isPreRelease())
                .filter(version -> version.supportsBuild(build))
                .max(Comparator.naturalOrder());
    }

}
