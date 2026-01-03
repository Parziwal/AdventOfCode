advent_of_code::solution!(4);

const DIRECTIONS: [(isize, isize); 8] = [
    (-1, -1),
    (-1, 0),
    (-1, 1),
    (0, -1),
    (0, 1),
    (1, -1),
    (1, 0),
    (1, 1),
];

fn parse_grid(input: &str) -> Vec<Vec<char>> {
    input.lines().map(|l| l.chars().collect()).collect()
}

fn count_neighbors(grid: &[Vec<char>], y: usize, x: usize) -> usize {
    let rows = grid.len() as isize;
    let cols = grid[0].len() as isize;
    let y = y as isize;
    let x = x as isize;

    DIRECTIONS
        .iter()
        .filter(|(dy, dx)| {
            let ny = y + dy;
            let nx = x + dx;
            ny >= 0 && nx >= 0 && ny < rows && nx < cols && grid[ny as usize][nx as usize] == '@'
        })
        .count()
}

pub fn part_one(input: &str) -> Option<u64> {
    let grid = parse_grid(input);

    let mut accessible = 0;
    for y in 0..grid.len() {
        for x in 0..grid[0].len() {
            if grid[y][x] == '@' && count_neighbors(&grid, y, x) < 4 {
                accessible += 1;
            }
        }
    }

    Some(accessible as u64)
}

pub fn part_two(input: &str) -> Option<u64> {
    let mut grid = parse_grid(input);
    let rows = grid.len();
    let cols = grid[0].len();
    let mut accessible = 0;

    loop {
        let mut to_remove = Vec::new();

        for y in 0..rows {
            for x in 0..cols {
                if grid[y][x] == '@' && count_neighbors(&grid, y, x) < 4 {
                    to_remove.push((y, x));
                }
            }
        }

        if to_remove.is_empty() {
            break;
        }

        for (y, x) in to_remove {
            grid[y][x] = '.';
            accessible += 1;
        }
    }

    Some(accessible)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(13));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(43));
    }
}
