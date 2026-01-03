advent_of_code::solution!(8);

struct Point3D {
    x: i64,
    y: i64,
    z: i64,
}

struct JunctionBox {
    id: usize,
    position: Point3D,
}

impl JunctionBox {
    fn squared_distance_to(&self, other: &JunctionBox) -> i64 {
        (other.position.x - self.position.x) * (other.position.x - self.position.x)
            + (other.position.y - self.position.y) * (other.position.y - self.position.y)
            + (other.position.z - self.position.z) * (other.position.z - self.position.z)
    }
}

fn parse_junction_boxes(input: &str) -> Vec<JunctionBox> {
    input
        .lines()
        .enumerate()
        .map(|(idx, line)| {
            let [x, y, z] = line
                .splitn(3, ',')
                .map(|coordinate| coordinate.parse::<i64>().unwrap())
                .collect::<Vec<_>>()
                .try_into()
                .unwrap();

            JunctionBox {
                id: idx,
                position: Point3D { x, y, z },
            }
        })
        .collect()
}

fn build_sorted_distances(junction_boxes: &[JunctionBox]) -> Vec<(usize, usize, i64)> {
    let mut distances = Vec::new();
    for i in 0..(junction_boxes.len() - 1) {
        for j in (i + 1)..junction_boxes.len() {
            distances.push((
                junction_boxes[i].id,
                junction_boxes[j].id,
                junction_boxes[i].squared_distance_to(&junction_boxes[j]),
            ));
        }
    }

    distances.sort_by_key(|dist| dist.2);
    distances
}

fn union_sets(
    junction_boxes: &mut [JunctionBox],
    count_by_positions: &mut [u64],
    id1: usize,
    id2: usize,
) {
    if id1 != id2 {
        count_by_positions[id1] += count_by_positions[id2];
        count_by_positions[id2] = 0;

        for jb in junction_boxes.iter_mut() {
            if jb.id == id2 {
                jb.id = id1
            }
        }
    }
}

pub fn part_one(input: &str) -> Option<u64> {
    let mut junction_boxes = parse_junction_boxes(input);
    let distances = build_sorted_distances(&junction_boxes);

    let mut count_by_positions = vec![1; junction_boxes.len()];
    for &(id1, id2, _) in distances
        .iter()
        .take(10_u64.pow(junction_boxes.len().ilog10()) as usize)
    {
        let id1 = junction_boxes[id1].id;
        let id2 = junction_boxes[id2].id;
        union_sets(&mut junction_boxes, &mut count_by_positions, id1, id2);
    }

    count_by_positions.sort();
    let multiplication_of_three_largest_circuits =
        count_by_positions.iter().rev().take(3).product::<u64>();

    Some(multiplication_of_three_largest_circuits)
}

pub fn part_two(input: &str) -> Option<u64> {
    let mut junction_boxes = parse_junction_boxes(input);
    let distances = build_sorted_distances(&junction_boxes);

    let mut count_by_positions = vec![1; junction_boxes.len()];
    let mut dist_iter = distances.iter();

    loop {
        if let Some(&(dist_id1, dist_id2, _)) = dist_iter.next() {
            let id1 = junction_boxes[dist_id1].id;
            let id2 = junction_boxes[dist_id2].id;

            union_sets(&mut junction_boxes, &mut count_by_positions, id1, id2);

            if count_by_positions.iter().filter(|&&d| d != 0).count() == 1 {
                let multi_of_x_of_last_two_boxes =
                    junction_boxes[dist_id1].position.x * junction_boxes[dist_id2].position.x;
                return Some(multi_of_x_of_last_two_boxes as u64);
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(40));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(25272));
    }
}
